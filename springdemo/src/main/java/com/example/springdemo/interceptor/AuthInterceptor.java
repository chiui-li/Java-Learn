package com.example.springdemo.interceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.net.DispatchType;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.springdemo.constants.Constants;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

  @Autowired
  private RedisTemplate<String, Object> redis;

  static List<String> excludePath = List.of("/user/login", "/user/register", "/ping");

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    //
    //
    if (request.getDispatcherType() == DispatcherType.ERROR) {
      return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    HttpSession session = request.getSession(true);

    if (excludePath.stream().anyMatch(m -> m.equals(request.getServletPath()))) {
      return true;
    }

    if (request.getMethod().toLowerCase().equals("options")) {
      return true;
    }
    log.info("拦截器 sessionId[{}] 请求path[{}]", session.getId(), request.getPathInfo());
    String redisSeessionID = (String) session.getAttribute(Constants.SEESSION_KEY);
    if (redisSeessionID == null || redisSeessionID != null && redisSeessionID.isEmpty()) {
      log.info("拦截器 redisSeessionID[{}] 没有登录态被拦截", redisSeessionID);
      response.setStatus(401);
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().write("{\"code\":0,\"errMsg\":\"未登录\"}");
      return false;
    }
    Object userIDMaybeNull = redis.opsForValue().get(redisSeessionID);
    if (userIDMaybeNull != null) {
      Long userID = ((Number) userIDMaybeNull).longValue();
      request.setAttribute(Constants.ATR_USERID, userID);
      redis.expire(redisSeessionID, Constants.EXPIRE_MIN, TimeUnit.MINUTES);
      return HandlerInterceptor.super.preHandle(request, response, handler);
    } else {
      log.info("拦截器 redisSeessionID[{}] 登录态过期", redisSeessionID);
      response.setStatus(401);
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().write("{\"code\":0,\"errMsg\":\"未登录\"}");
      return false;
    }
  }
}
