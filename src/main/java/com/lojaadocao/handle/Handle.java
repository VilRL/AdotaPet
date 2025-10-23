package com.lojaadocao.handle;

public interface Handle<T, R> {
    R executar(T parametro);
}
