package com.example.springdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.springdemo.entity.PostEntity;

@Mapper
public interface PostMapper {

  public int createPost(
      @Param("post") PostEntity post);

  public PostEntity getDraftArticleById(
      @Param("userID") Long userID,
      @Param("articleId") Long articleId);

  public int updateDraftArticleById(
      @Param("p") PostEntity p);
}
