package com.example.springdemo.entity;

import lombok.Data;

@Data
public class CategoryEntity {
  private Long id;
  private String name;
  private Long userID;
}
