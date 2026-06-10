package com.example.springdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

  // @Select("SELECT * FROM posts WHERE user_id = (SELECT id FROM users WHERE
  // username = #{userName}) AND status = 'published'")
  // public List<PostEntity> listPublishedPosts(@Param("userName") String
  // userName);

  // @Select("SELECT * FROM posts WHERE user_id = #{userID} AND categoryId =
  // #{categoryId} AND status = 'published' ORDER BY updated_at DESC")
  public List<PostEntity> listPublishedPosts(@Param("userID") Long userID, @Param("categoryId") Long categoryId);

  public PostEntity getPublishedPostById(@Param("postId") Long postId);

  public int incrementViewCount(@Param("postId") Long postId);
}
