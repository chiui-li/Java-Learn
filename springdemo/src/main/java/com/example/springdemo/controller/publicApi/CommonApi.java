package com.example.springdemo.controller.publicApi;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springdemo.constants.Constants;
import com.example.springdemo.entity.PostEntity;
import com.example.springdemo.entity.User;
import com.example.springdemo.services.CategoryService;
import com.example.springdemo.services.PostService;
import com.example.springdemo.services.UserService;
import com.example.springdemo.utils.Page;
import com.example.springdemo.utils.Result;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CommonApi {

  @Autowired
  public RedisTemplate<String, String> redisTemplate;

  @Autowired
  public PostService postService;

  @Autowired
  public UserService userService;
  @Autowired
  public CategoryService categoryService;

  @GetMapping("/blog/{userName}")
  public String index(@PathVariable String userName, Model model,
      @RequestParam(value = "categoryId", required = false) Long categoryId,
      @RequestParam(value = "categoryName", required = false) String categoryName,
      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
    model.addAttribute("userName", userName);
    model.addAttribute("activeCategoryId", categoryId);
    model.addAttribute("activeCategoryName", categoryName);
    String cachedUserIdStr = redisTemplate.opsForValue().get(Constants.USERNAME_2_USERID + ":" + userName);
    Long cachedUserId = cachedUserIdStr != null ? Long.parseLong(cachedUserIdStr) : 0L;
    if (cachedUserId == null || cachedUserId <= 0) {
      User user = userService.findUserByUsername(userName);
      if (user != null) {
        redisTemplate.opsForValue().set(Constants.USERNAME_2_USERID + ":" + userName, user.getId().toString());
        cachedUserId = user.getId();
      }
    }

    User user = userService.findById(cachedUserId);
    model.addAttribute("profile", user);
    Page<PostEntity> page = postService.listPublishedPostsPage(cachedUserId, categoryId, pageNum, pageSize);
    model.addAttribute("posts", page.getList());
    model.addAttribute("page", page);
    model.addAttribute("categories", categoryService.selectAllPublished(cachedUserId));
    return "index";
  }

  @GetMapping("/blog/{userName}/post/{postId}/{postTitle}.html")
  public String postDetail(@PathVariable String userName, @PathVariable Long postId, @PathVariable String postTitle,
      Model model) {
    model.addAttribute("userName", userName);
    String cachedUserIdStr = redisTemplate.opsForValue().get(Constants.USERNAME_2_USERID + ":" + userName);
    Long cachedUserId = cachedUserIdStr != null ? Long.parseLong(cachedUserIdStr) : 0L;
    if (cachedUserId == null || cachedUserId <= 0) {
      User user = userService.findUserByUsername(userName);
      if (user != null) {
        redisTemplate.opsForValue().set(Constants.USERNAME_2_USERID + ":" + userName, user.getId().toString());
        cachedUserId = user.getId();
      }
    }

    PostEntity post = postService.getPublishedPostById(postId);
    if (post == null) {
      return "error/404";
    }
    postService.incrementViewCount(postId);
    post.setViewCount(post.getViewCount() + 1);

    User profile = userService.findById(cachedUserId);
    model.addAttribute("profile", profile);
    model.addAttribute("post", post);
    return "post";
  }

  @GetMapping("/")
  public String home(Model model) {
    List<User> users = userService.selectAllUsers();
    model.addAttribute("users", users);
    return "home";
  }

  @GetMapping("/getFile/{fileName}")
  public void getMethodName(@PathVariable String fileName, HttpServletResponse response) throws IOException {
    File file = new File("uploadFile" + "/" + fileName);
    response.setContentType("image/jpeg");
    String mimeType = Files.probeContentType(file.toPath());
    if (mimeType == null) {
      mimeType = "application/octet-stream";
    }
    response.setContentType(mimeType);
    response.setHeader("Content-Disposition",
        "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
    response.setHeader(fileName, mimeType);
    response.setHeader("Cache-Control", "public, max-age=31536000, immutable");
    try {
      Files.copy(file.toPath(), response.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
