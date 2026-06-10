package com.example.springdemo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springdemo.entity.CategoryEntity;
import com.example.springdemo.entity.PostEntity;
import com.example.springdemo.mapper.CategoryMapper;
import com.example.springdemo.mapper.PostMapper;
import com.example.springdemo.utils.Page;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {

  // private final CategoryService categoryService;
  @Autowired
  private PostMapper postMapper;
  @Autowired
  private CategoryMapper categoryMapper;

  PostService() {
    // this.categoryService = categoryService;
  }

  @Transactional
  public Long createPost(PostEntity p) {
    String cName = p.getCategoryName();
    Long userID = p.getUserID();
    CategoryEntity c = categoryMapper.selectByName(userID, cName);
    if (c == null) {
      CategoryEntity newC = new CategoryEntity();
      newC.setUserID(userID);
      newC.setName(cName);
      categoryMapper.addCategory(newC);
      p.setCategoryId(newC.getId());
    } else {
      p.setCategoryId(c.getId());
    }
    postMapper.createPost(p);
    return p.getId();
  }

  public PostEntity getDraftArticleById(Long userId, Long articleId) {
    return postMapper.getDraftArticleById(userId, articleId);
  }

  @Transactional
  public int updateDraftArticleById(PostEntity p) {
    String cName = p.getCategoryName();
    Long userID = p.getUserID();
    CategoryEntity c = categoryMapper.selectByName(userID, cName);
    if (c == null) {
      CategoryEntity newC = new CategoryEntity();
      newC.setUserID(userID);
      newC.setName(cName);
      categoryMapper.addCategory(newC);
      p.setCategoryId(newC.getId());
    } else {
      p.setCategoryId(c.getId());
    }
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

  public Boolean delPost(Long userID, Long articleId) {
    if (postMapper.delPostById(articleId, userID) > 0) {
      return true;
    }
    return false;
  }
}
