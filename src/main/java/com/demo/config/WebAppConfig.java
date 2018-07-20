package com.demo.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-19 19:45
 */
@SpringBootConfiguration
public class WebAppConfig extends WebMvcConfigurationSupport {

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
}
