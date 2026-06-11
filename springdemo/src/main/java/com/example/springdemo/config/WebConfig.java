package com.example.springdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.springdemo.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private AuthInterceptor authInterceptor;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 允许跨域的路径
        .allowedOriginPatterns("*") // 允许的域名来源，* 代表允许所有
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
        .allowedHeaders("*") // 允许的请求头
        .allowCredentials(true) // 是否允许携带Cookie
        .maxAge(3600); // 预检请求的缓存时间(秒)
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authInterceptor)
        .addPathPatterns("/**") // 拦截所有请求
        .excludePathPatterns("/", "/user/login", "/user/register", "/ping", "/blog/**", "/page/**", "/static/**",
            "/favicon.ico",
            "/*.html",
            "/*.js",
            "/css/**",
            "/*.*"); // 排除登录/注册等接口
  }

}
