package com.example.springdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.springdemo.entity.CategoryEntity;

@Mapper
public interface CategoryMapper {
  @Insert("insert into category(name, user_id) values(#{c.name}, #{c.userID})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  public int addCategory(@Param("c") CategoryEntity c);

  @Select("select * from category where user_id=#{userID}")
  public List<CategoryEntity> selectAll(@Param("userID") Long userID);

  @Select("select * from category where user_id=#{userID} and name=#{name}")
  public CategoryEntity selectByName(@Param("userID") Long userID, @Param("name") String name);
}
