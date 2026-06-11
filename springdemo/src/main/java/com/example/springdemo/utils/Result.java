package com.example.springdemo.utils;

import io.micrometer.common.lang.Nullable;

public record Result<T>(int code, String errMsg, T data) {
  public static <M> Result<M> success(M data, @Nullable String errMsg) {
    return new Result<>(1, errMsg, data);
  }

  public static <M> Result<M> error(String errMsg) {
    return new Result<M>(0, errMsg, null);
  }

}
