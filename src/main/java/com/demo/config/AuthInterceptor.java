package com.demo.config;

import com.alibaba.dubbo.rpc.RpcContext;
import com.demo.units.Md5Unit;
import com.demo.vo.LoginVo;
import com.demo.vo.RequestIp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Map;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-19 20:26
 */
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private static String respResult = "{\"resultCode\":401,\"resultMsg\":\"no login\"}";
    private static String respResultKeyword = "{\"resultCode\":406,\"resultMsg\":\"Illegal keyword arguments\"}";
    private static final String SSO_LOGIN_ACCOUNT = "sso.login.account.auth";
    private static final int NOLOGIN = 401;
    private static final int INKEY = 406;
    private static final int SERVICE_UNAVAILABLE = 503;
    private PrintWriter out;
    private static String[] sqlKeyword = new String[]{"exec ", "execute ", "insert ", "create ", "drop ", " table ", " from ", "grant ", "use ", "group_concat ", "column_name ", "information_schema.columns ", "table_schema ", "union ", " where ", "select ", "delete ", "update ", " order by ", " count( ", "chr ", "mid ", "alert ", "master ", "having ", "truncate ", "char ", "declare ", "or ", "like ", " '", "-- ", "' ", "(select ", "/*", "*/", "+ "};
    private static final Long EXPIRETIME = 1800000L;

    @Autowired
    private RedisConfig redisConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        logger.info("---------------------开始进入请求地址拦截----------------------------");
        out = response.getWriter();

        //频繁访问拦截
        if(!serviceUnavailable(request)){
            return false;
        }

        //SQL关键拦截
        if (!checkRequestParam(request)) {
            return false;
        }

        String auth = getAuth(request);
        String sign = request.getHeader("sign");
        String timestamp = request.getHeader("timestamp");

        //非法请求拦截
        if(StringUtils.isEmpty(auth) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(timestamp)){
            Writer(NOLOGIN);
            return false;
        }

        Long timeMillis = System.currentTimeMillis() - getLongTimestamp(timestamp);

        //非法请求拦截
        if(timeMillis < 0 || timeMillis > 300000){
            Writer(SERVICE_UNAVAILABLE, "请求已过期，请重新提交！");
            return false;
        }

        //登录验证请求拦截
        String newSign = getSign(auth, timestamp);
        if(!newSign.equals(sign)){
            Writer(NOLOGIN);
            return false;
        }

        Object obj = redisConfig.get(auth);
        if(obj instanceof LoginVo){
            redisConfig.setExpireTime(auth, EXPIRETIME);
            RpcContext context = RpcContext.getContext();
            LoginVo vo = (LoginVo)obj;
            context.set("userId", vo.getUserId());
            context.set("userName", vo.getUserName());
            return true;
        }
        return false;
    }

    private boolean serviceUnavailable(HttpServletRequest request){
        //获取真实IP地址
        String ip  =  request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getHeader(" Proxy-Client-IP ");
        }
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getHeader(" WL-Proxy-Client-IP ");
        }
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        //取session中的IP对象
        RequestIp re = (RequestIp)request.getSession().getAttribute(ip);

        if(null == re){
            //放入到session中
            RequestIp reIp = new RequestIp();
            reIp.setCreateTime(System.currentTimeMillis());
            reIp.setReCount(1);
            request.getSession().setAttribute(ip,reIp);
        } else {
            Long createTime = re.getCreateTime();
            if(null == createTime){
                //时间请求为空
                Writer(SERVICE_UNAVAILABLE, "请求太快，请稍后再试！");
                return false;
            } else {
                if(((System.currentTimeMillis() - createTime)/1000) > 3){
                    //当前时间离上一次请求时间大于3秒，可以直接通过,保存这次的请求
                    RequestIp reIp = new RequestIp();
                    reIp.setCreateTime(System.currentTimeMillis());
                    reIp.setReCount(1);
                    request.getSession().setAttribute(ip,reIp);
                } else {
                    if(re.getReCount() > 10){
                        Writer(SERVICE_UNAVAILABLE, "请求太快，请稍后再试！");
                        return false;
                    }else{
                        //小于3秒，但请求数小于10次，给对象添加
                        re.setCreateTime(System.currentTimeMillis());
                        re.setReCount(re.getReCount()+1);
                        request.getSession().setAttribute(ip,re);
                    }
                }
            }
        }
        return true;
    }

    private Long getLongTimestamp(String timestamp){
        if(StringUtils.isEmpty(timestamp)){
            return 0L;
        }
        try{
            return Long.parseLong(timestamp);
        } catch (Exception e){
            logger.error("无法将客户端时间戳timestamp转换为Long，timestamp：" + timestamp);
            return 0L;
        }
    }

    /**
     * 获取Auth
     * @param request
     * @return
     */
    private String getAuth(HttpServletRequest request) {
        String auth = request.getHeader("auth");
        if(StringUtils.isEmpty(auth)){
            auth = request.getParameter("auth");
            if(StringUtils.isEmpty(auth)){
                Cookie[] cookies = request.getCookies();
                if(cookies != null){
                    for (Cookie cookie : cookies){
                        if(cookie.getName().equals(SSO_LOGIN_ACCOUNT)){
                            auth = cookie.getValue();
                            break;
                        }
                    }
                }
            }
        }
        return auth;
    }

    /**
     * 检查参数
     * @param request
     * @return
     */
    private boolean checkRequestParam(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()){
            String[] params = map.get(key);
            if (params != null && params.length > 0) {
                for (String param : params) {
                    if (sqlValidate(param)) {
                        Writer(INKEY);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 验证请求参数是否带有Sql关键字
     *
     * @param param 请求参数
     * @return true：含有sql关键字，false不含
     */
    private boolean sqlValidate(String param) {
        if (StringUtils.isEmpty(param)) {
            return false;
        }

        for (String sql : sqlKeyword) {
            if (param.toLowerCase().contains(sql)) {
                logger.error("请求被拦截======>参数中含有SQL关键字：{}", sql);
                return true;
            }
        }

        return false;
    }


    private void Writer(int code, String... msg) {
        switch (code) {
            case 401:
                out.write(respResult);
                out.close();
                return;
            case 406:
                out.write(respResultKeyword);
                out.close();
                return;
            default:
                String message = "";
                if(msg != null && msg.length > 0){
                    message = String.join(",", msg);
                } else if (msg != null) {
                    message = msg.toString();
                }
                out.write("{\"resultCode\":" + code + ",\"resultMsg\":\"" + message + "\"}");
                out.close();
        }
    }

    private String getSign(String auth, String timestamp){
        String newStr = auth + timestamp;
        return Md5Unit.md5(newStr);
    }
}
