package com.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.config.RedisConfig;
import com.demo.controller.tool.RestResponse;
import com.demo.pojo.DemoMembersPojo;
import com.demo.service.IUserService;
import com.demo.units.Md5Unit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 14:40
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private IUserService userService;

    @Resource
    private RedisConfig redisConfig;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public RestResponse login(@RequestParam String userName, @RequestParam String pwd){
        DemoMembersPojo user = userService.getUserByUserName(userName);
        if(Md5Unit.md5(pwd).toLowerCase().equals(user.getPwd().toLowerCase())){
            String userJson = JSONObject.toJSON(user).toString();
            String auth = Md5Unit.md5(userJson);
            redisConfig.set(auth, user, 1800000L);
            return new RestResponse(auth);
        } else {
            return new RestResponse(1, "用户名或密码错误！");
        }
    }

    @RequestMapping("/detail")
    public RestResponse get(@RequestParam Long id){
        DemoMembersPojo user = userService.getUserById(id);
        return new RestResponse(user);
    }

    @RequestMapping("/redis-detail")
    public RestResponse get(@RequestParam String auth){
        DemoMembersPojo user = (DemoMembersPojo)redisConfig.get(auth);
        return new RestResponse(user);
    }
}
