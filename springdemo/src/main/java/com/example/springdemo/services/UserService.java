package com.example.springdemo.services;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springdemo.entity.User;
import com.example.springdemo.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    UserMapper uMapper;

    public User findByPassword(String name, String ps) {
        return uMapper.findUserByPassword(name, ps);
    }

    public User findById(Long id) {
        return uMapper.findUserById(id);
    }

    public List<User> selectAllUsers() {
        return uMapper.selectUserList();
    }
}
