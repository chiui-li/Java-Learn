package com.example.springdemo.utils;

import io.micrometer.common.lang.Nullable;

public record Result<T>(int code, String msg, T data) {
    public static <M> Result<M> success(M data, @Nullable String msg) {
        return new Result<>(1, msg == null ? "ok" : msg, data);
    }

    public static <M> Result<M> error(String msg) {
        return new Result<M>(0, msg, null);
    }

}
