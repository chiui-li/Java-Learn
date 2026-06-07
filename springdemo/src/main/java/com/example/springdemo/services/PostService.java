package com.example.springdemo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springdemo.entity.PostEntity;
import com.example.springdemo.mapper.PostMapper;
import com.example.springdemo.utils.Page;
import com.github.pagehelper.PageHelper;

@Service
public class PostService {

  @Autowired
  private PostMapper postMapper;

  public Long createPost(PostEntity p) {
    postMapper.createPost(p);
    if (p.getId() != null) {
      return p.getId();
    }
    return null;
  }

  public PostEntity getDraftArticleById(Long userId, Long articleId) {
    return postMapper.getDraftArticleById(userId, articleId);
  }

  public int updateDraftArticleById(PostEntity p) {
    return postMapper.updateDraftArticleById(p);
  }

  public int publishArticleById(PostEntity p) {
    return postMapper.publishArticleById(p);
  }

  public int unpublishArticleById(PostEntity p) {
    return postMapper.unpublishArticleById(p);
  }

  public Page<PostEntity> listPosts(Long userID, String keyword, int pageNum, int pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    List<PostEntity> postList = postMapper.listPosts(userID, keyword);
    Page<PostEntity> pageInfo = new Page<PostEntity>(postList);
    return pageInfo;
  }
}
