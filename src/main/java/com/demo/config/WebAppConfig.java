package com.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-19 19:45
 */
@SpringBootConfiguration
public class WebAppConfig extends WebMvcConfigurationSupport {
    private static final Logger logger = LoggerFactory.getLogger(WebAppConfig.class);
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration authRegistration = registry.addInterceptor(authInterceptor()).addPathPatterns("/api/**");
        InterceptorRegistration permissionRegistration = registry.addInterceptor(permissionInterceptor()).addPathPatterns("/api/**");
        List<String> excludeList = new ArrayList<>();
        excludeList.add("/*/user/login");
        excludeList.add("/*/user/detail");
        excludeList.add("/*/user/redis-detail");

        authRegistration.excludePathPatterns(excludeList);
        permissionRegistration.excludePathPatterns(excludeList);
    }

    @Bean
    public AuthInterceptor authInterceptor(){
        return new AuthInterceptor();
    }

    @Bean
    public PermissionInterceptor permissionInterceptor(){
        return new PermissionInterceptor();
    }

    @Bean
    public Converter<String, Date> dateConverter(){
        return source -> {
            if(StringUtils.isEmpty(source)){
                return null;
            }

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date;
            try {
                date = sdf.parse(source);
            } catch (Exception e) {
                logger.error(e.getMessage());
                date = new Date();
            }
            return date;
        };
    }
}
