package com.example.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.entity.CategoryEntity;
import com.example.springdemo.services.CategoryService;
import com.example.springdemo.utils.Result;

@RestController
@RequestMapping("/category")
public class CategoryController {

  @Autowired
  CategoryService categoryService;

  @PostMapping("/add")
  public Result<Boolean> addCategory(
      @RequestBody CategoryEntity category, @RequestAttribute Long userID) {
    category.setUserID(userID);
    if (categoryService.addCategory(category) > 0) {
      return Result.success(true, null);
    }
    return Result.success(false, "添加失败");
  }

  @GetMapping("/all")
  public Result<List<CategoryEntity>> postMethodName(@RequestAttribute Long userID) {
    return Result.success(categoryService.selectAll(userID), null);
  }

}
