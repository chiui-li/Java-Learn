package com.example.springdemo.controller.user;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.constants.Constants;
import com.example.springdemo.entity.User;
import com.example.springdemo.services.UserService;
import com.example.springdemo.utils.Result;
import com.qiniu.util.Auth;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/user")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService uService;

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @RequestMapping("/login")
  public Result<User> login(@RequestBody User user, HttpSession session) {
    User u = uService.findByPassword(user.getUsername(), user.getPassword());
    if (u != null) {
      Long userId = u.getId();
      String uuid = UUID.randomUUID().toString();
      if (uuid != null && userId != null) {
        // session
        session.setAttribute(Constants.SEESSION_KEY, uuid);
        // 再设置过期时间
        logger.info("登录成功 userId:[{}]  redisKey: {}", userId, uuid, userId);
        redisTemplate.opsForValue().set(uuid, userId);
        redisTemplate.expire(uuid, Constants.EXPIRE_MIN, TimeUnit.MINUTES);
        return Result.success(u, null);
      } else {
        return Result.success(null, "password error");
      }
    } else {
      return Result.success(null, "password error");
    }
  }

  @PostMapping("/register")
  public Result<Boolean> register(@RequestBody User u) {

    switch (uService.registerUser(u)) {
      case "error":
        return Result.success(false, "注册错误");
      case "email":
        return Result.success(false, "邮箱已经被注册");
      case "username":
        return Result.success(false, "用户名已经被注册");
      case "new":
        return Result.success(true, null);
      default:
        return Result.success(false, "注册错误");
    }
  }

  @GetMapping("/who")
  public Result<User> whoAmI(@RequestAttribute("userID") Long userID) {
    User u = uService.findById(userID);
    if (u != null) {
      u.setId(null);
      return Result.success(u, null);
    }
    return Result.error("你是谁?");
  }

  @GetMapping("/logout")
  public Result<Boolean> logout(HttpSession session, @RequestAttribute("redis_user_session_key") String redisKey) {
    session.invalidate();
    Integer s = (Integer) redisTemplate.opsForValue().getAndDelete(redisKey);

    logger.info("注销用户: {}", s);

    return Result.success(true, null);
  }

  @GetMapping("/upload/getToken")
  public Result<String> getMethodName() {
    String accessKey = System.getenv("QINIU_ACCESS_KEY");
    String secretKey = System.getenv("QINIU_SECRET_KEY");
    Auth auth = Auth.create(accessKey, secretKey);
    return Result.success(auth.uploadToken("chiuili-blog"), null);
  }

}
