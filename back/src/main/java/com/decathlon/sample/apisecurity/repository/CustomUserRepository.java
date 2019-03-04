package com.decathlon.sample.apisecurity.repository;

import com.decathlon.sample.apisecurity.model.User;

import java.util.List;

public interface CustomUserRepository {

    List<User> findByCredentialsUnsafe(String login, String password);
}
