package com.example.springdemo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springdemo.entity.CategoryEntity;
import com.example.springdemo.mapper.CategoryMapper;

@Service
public class CategoryService {

  @Autowired
  public CategoryMapper cMapper;

  public Long addCategory(CategoryEntity c) {
    cMapper.addCategory(c);
    return c.getId();
  }

  public List<CategoryEntity> selectAll(Long userID) {
    return cMapper.selectAll(userID);
  }

  public List<CategoryEntity> selectAllPublished(Long userID) {
    return cMapper.selectAllPublished(userID);
  }

}
