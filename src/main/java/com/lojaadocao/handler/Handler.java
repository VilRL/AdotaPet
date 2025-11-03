package com.lojaadocao.handler;

public interface Handler<T, R> {
    R execute(T parameter);
}