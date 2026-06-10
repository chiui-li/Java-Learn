package com.example.springdemo.services;

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

  public User findUserByUsername(String username) {
    return uMapper.findUserByUsername(username);
  }

  public List<User> selectAllUsers() {
    return uMapper.selectUserList();
  }

  public String registerUser(User u) {
    List<User> users = uMapper.findUserByEmail(u.getEmail(), u.getUsername());
    if (users.isEmpty()) {
      try {
        uMapper.addUser(u);
        return "new";
      } catch (Exception e) {
        return "error";
      }
    }
    Boolean hasEmail = users.stream().anyMatch(user -> {
      return user.getEmail().toLowerCase().equals(u.getEmail());
    });

    if (hasEmail) {
      return "email";
    }

    Boolean hasUsername = users.stream().anyMatch(user -> {
      return user.getUsername().toLowerCase().equals(u.getUsername());
    });

    if (hasUsername) {
      return "username";
    }

    return "error";
  }
}
