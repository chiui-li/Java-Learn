package com.example.springdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.utils.Result;

@RestController
public class Ping {
    @RequestMapping("/ping")
    public Result<Object> ping() {
        return Result.success("pong", null);
    }
}
