package com.example.springdemo.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.constants.Constants;
import com.example.springdemo.entity.User;
import com.example.springdemo.services.UserService;
import com.example.springdemo.utils.Result;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

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
            System.out.println("----->" + userId);
            if (uuid != null && userId != null) {
                session.setAttribute(Constants.SEESSION_KEY, uuid);
                // 再设置过期时间
                System.out.println("登录成功 -----> " + uuid);
                redisTemplate.opsForValue().set(uuid, userId);
                redisTemplate.expire(uuid, Constants.EXPIRE_MIN, TimeUnit.MINUTES);
                return Result.success(u, null);
            } else {
                return Result.error("password error");
            }
        } else {
            return Result.error("password error");
        }
    }

    @RequestMapping("/all")
    public Result<List<User>> queryUsers() {
        return Result.success(uService.selectAllUsers(), null);
    }
}
