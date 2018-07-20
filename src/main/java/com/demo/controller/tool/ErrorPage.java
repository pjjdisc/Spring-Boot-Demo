package com.demo.controller.tool;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 17:00
 */
@Controller
public class ErrorPage implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String heandleError(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode){
            case 401:
                return "/login";
            case 404:
                return "/errorPage/404";
            default:
                return "/errorPage/500";
        }
    }
}
