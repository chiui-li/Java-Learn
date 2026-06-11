package com.example.springdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EnvMsg {
  @Value("${spring.datasource.url}")
  private String datasourceUrl;

  @Value("${spring.datasource.username}")
  private String datasourceUsername;

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @PostConstruct
  public void printConfig() {
    System.out.println("=== 配置变量检查 ===");
    System.out.println("数据库URL: " + datasourceUrl);
    System.out.println("数据库用户名: " + datasourceUsername);
    System.out.println("Redis主机: " + redisHost);
    System.out.println("=================");
  }
}
