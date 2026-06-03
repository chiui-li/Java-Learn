package com.example.springdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springdemo.entity.User;

@Mapper
public interface UserMapper {
    List<User> selectUserList();
}
