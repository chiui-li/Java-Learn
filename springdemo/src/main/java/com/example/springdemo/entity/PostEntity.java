package com.example.springdemo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PostEntity implements Serializable {
  /**
   * 主键ID
   */
  private Long id;

  @JsonIgnore
  private Long userID;
  private Long categoryId;
  private String categoryName;
  private String contentHtml;

  private String title;
  private String content;
  private String draftTitle;
  private String draftContent;
  private String postType;
  private String status;
  private Long viewCount;
  private String description;
  private String coverUrl;
  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime createdAt;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime updatedAt;

  /**
   * 草稿更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime draftUpdatedAt;

}
