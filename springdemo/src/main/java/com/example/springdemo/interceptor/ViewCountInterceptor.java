package com.example.springdemo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.springdemo.mapper.ViewsMapper;
import com.example.springdemo.utils.IpUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ViewCountInterceptor implements HandlerInterceptor {
  // @Autowired
  // private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ViewsMapper vMapper;

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView) {

    String ip = IpUtil.getClientIp(request);
    String path = request.getRequestURI();
    vMapper.addLog(path, ip);
  }

}
