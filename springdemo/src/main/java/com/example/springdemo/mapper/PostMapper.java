package com.example.springdemo.mapper;

import java.util.List;

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

  public int publishArticleById(
      @Param("p") PostEntity p);

  public int unpublishArticleById(
      @Param("p") PostEntity p);

  public List<PostEntity> listPosts(@Param("userID") Long userID, @Param("keyword") String keyword);

  public int delPostById(@Param("userID") Long userID, @Param("articleId") Long articleId);
}
