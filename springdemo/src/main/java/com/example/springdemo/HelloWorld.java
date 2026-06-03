package com.example.springdemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.entity.User;
import com.example.springdemo.mapper.UserMapper;

@RestController
public class HelloWorld {

    @Autowired(required = true)
    private UserMapper userMapper;

    public HelloWorld() {
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/users")
    public List<User> getUser() {
        return userMapper.selectUserList();
    }
}
