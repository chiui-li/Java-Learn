package com.example.springdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.springdemo.entity.User;

@Mapper
public interface UserMapper {
  public List<User> selectUserList();

  public Boolean addUser(User user);

  public Boolean delUser(Long id);

  public User findUserByPassword(
      @Param("username") String username,
      @Param("password") String password);

  public User findUserById(
      @Param("id") Long id);

  public List<User> findUserByEmail(
      @Param("email") String email, @Param("username") String username);

  public User findUserByUsername(@Param("username") String username);

  public int updateUser(@Param("p") User user);
}
