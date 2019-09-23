package com.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 配置跨域支持拦截器
 * @author 彭方林
 */
@Configuration
public class CrossConfig implements WebMvcConfigurer{
	/**
     * 注册 拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor(){
          /**
           * controller 执行之前调用
           */
          @Override
          public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Type", "text/json;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            request.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
            response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
            response.setHeader("X-Powered-By","Jetty");
            return true;
          }
        });
    }
}
