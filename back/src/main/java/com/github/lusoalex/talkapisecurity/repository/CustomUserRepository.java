package com.github.lusoalex.talkapisecurity.repository;

import com.github.lusoalex.talkapisecurity.model.User;

import java.util.List;

public interface CustomUserRepository {

    List<User> findByCredentialsUnsafe(String login, String password);
}
