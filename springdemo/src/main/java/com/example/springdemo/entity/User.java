package com.example.springdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class User {

  /**
   * 主键ID
   */
  private Long id;

  /**
   * 用户名
   */
  private String username;

  private String avatar;
  private String bio;

  /**
   * GitHub 主页
   */
  private String github;

  /**
   * 个人网站
   */
  private String website;

  /**
   * 密码(加密后)
   */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  /**
   * 邮箱
   */
  private String email;

  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime createdAt;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime updatedAt;

  /**
   * 密码更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime passwordUpdatedAt;

}
