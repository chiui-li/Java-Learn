package com.example.springdemo.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.pagehelper.PageInfo;

@JsonIgnoreProperties({ "navigatePages", "navigatepageNums", "navigateFirstPage",
    "navigateLastPage", "firstPage", "lastPage", "nextPage",
    "prePage", "startRow", "endRow", "size", "isLastPage",
    "isFirstPage", "hasPreviousPage", "hasNextPage" })
public class Page<T> extends PageInfo<T> {

  public Page(List<T> list) {
    super(list);
  }

  public Page() {
    super();
  }

}
