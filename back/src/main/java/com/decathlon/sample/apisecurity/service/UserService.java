package com.decathlon.sample.apisecurity.service;

import com.decathlon.sample.apisecurity.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {

    Mono<List<User>> authenticate(String login, String password);
    Flux<User> findlAll();
    Flux<User> save(List<User> list);
    Mono<User> register(User user);
    Mono<User> findById(String id);
    Mono<User> update(User user);
    Mono<Void> delete(String id);
    String buildJwt(User user);
}
