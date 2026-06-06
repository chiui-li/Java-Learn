package com.example.springdemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springdemo.entity.PostEntity;
import com.example.springdemo.mapper.PostMapper;

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
}
