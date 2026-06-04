package com.example.springdemo.interceptor;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.springdemo.constants.Constants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

  @Autowired
  private RedisTemplate<String, Object> redis;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    HttpSession session = request.getSession(true);
    String redisSeessionID = (String) session.getAttribute(Constants.SEESSION_KEY);
    System.out.println("-------->" + redisSeessionID);
    if (redisSeessionID == null || redisSeessionID != null && redisSeessionID.isEmpty()) {
      response.setStatus(401);
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().write("{\"code\":0,\"msg\":\"未登录\"}");
      return false;
    }
    Object userIDMaybeNull = redis.opsForValue().get(redisSeessionID);
    if (userIDMaybeNull != null) {
      Integer userID = (Integer) userIDMaybeNull;
      request.setAttribute(Constants.ATR_USERID, userID);
      redis.expire(redisSeessionID, Constants.EXPIRE_MIN, TimeUnit.MINUTES);
      return HandlerInterceptor.super.preHandle(request, response, handler);
    } else {
      response.setStatus(401);
      response.setContentType("application/json");
      response.getWriter().write("{\"code\":0,\"msg\":\"未登录\"}");
      return false;
    }
    // return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
