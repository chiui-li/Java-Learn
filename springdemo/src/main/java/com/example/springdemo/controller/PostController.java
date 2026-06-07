package com.example.springdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.entity.PostEntity;
import com.example.springdemo.services.PostService;
import com.example.springdemo.utils.Page;
import com.example.springdemo.utils.Result;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/posts")
public class PostController {

  @Autowired
  PostService pService;

  @PostMapping("/create")
  public Result<Long> createPosts(@RequestBody PostEntity p, @RequestAttribute("userID") Long userID) {
    p.setUserID(userID);
    Long newId = pService.createPost(p);
    if (newId == null) {
      return Result.success(null, "创建失败");
    }
    return Result.success(newId, null);
  }

  @GetMapping("/detail/{id}")
  public Result<PostEntity> getDraftArticleById(@PathVariable Long id, @RequestAttribute("userID") Long userID) {
    PostEntity p = pService.getDraftArticleById(userID, id);
    if (p != null) {
      return Result.success(p, null);
    }
    return Result.error("暂无文章");
  }

  @PostMapping("/detail/update/{id}/draft")
  public Result<Boolean> updateDraft(@RequestAttribute("userID") Long userID, @PathVariable Long id,
      @RequestBody PostEntity p) {
    p.setUserID(userID);
    if (pService.updateDraftArticleById(p) > 0) {
      return Result.success(true, null);
    }
    return Result.error("保存失败");
  }

  @PostMapping("/detail/publish/{id}")
  public Result<Boolean> publishArticle(@RequestAttribute("userID") Long userID, @PathVariable Long id,
      @RequestBody PostEntity p) {
    p.setUserID(userID);
    if (pService.publishArticleById(p) > 0) {
      return Result.success(true, null);
    }
    return Result.error("发布失败");
  }

  @PostMapping("/detail/unpublish/{id}")
  public Result<Boolean> unpublishArticle(@RequestAttribute("userID") Long userID, @PathVariable Long id) {
    PostEntity p = new PostEntity();
    p.setUserID(userID);
    p.setId(id);
    if (pService.unpublishArticleById(p) > 0) {
      return Result.success(true, null);
    }
    return Result.error("处理失败");
  }

  @GetMapping("/list")
  public Result<Page<PostEntity>> listPosts(
      @RequestAttribute("userID") Long userID,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "1") int pageNum,
      @RequestParam(defaultValue = "") String keyword) {
    Page<PostEntity> posts = pService.listPosts(userID, keyword, pageNum, pageSize);
    if (posts != null) {
      return Result.success(posts, null);
    }
    return Result.error("暂无文章");
  }
}
