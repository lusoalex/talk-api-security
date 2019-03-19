package com.github.lusoalex.talkapisecurity.service.impl;

import com.github.lusoalex.talkapisecurity.model.User;
import com.github.lusoalex.talkapisecurity.repository.UserRepository;
import com.github.lusoalex.talkapisecurity.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${jwt.secret:decat}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<List<User>> authenticate(String login, String password) {
        return Mono.just(userRepository.findByCredentialsUnsafe(login,password));
    }

    @Override
    public Flux<User> findlAll() {
        return Flux.fromIterable(userRepository.findAll());
    }

    @Override
    public Mono<User> register(User user) {
        return Mono.just(user)
                .map(userRepository::save);/*
                .onErrorResume(throwable -> {
                    if(throwable instanceof ConstraintViolationException) {
                        return Mono.error(new Exception("Please choose another username."));
                    }
                    return Mono.error(throwable);
                });*/
    }

    @Override
    public Mono<User> findById(String id) {
        return Mono.just(userRepository.findBySubject(id));
    }

    @Override
    public Mono<User> update(User user) {
        return Mono.just(userRepository.save(user));
    }

    @Override
    public Mono<Void> delete(String id) {
        return null;
    }

    @Override
    public Flux<User> save(List<User> list) {
        return Flux.fromIterable(userRepository.saveAll(list));
    }

    @Override
    public String buildJwt(User user) {
        return Jwts.builder()
                .setIssuer("https://decathlon.ca")
                .setAudience("security-api-talk")
                .setSubject(user.getId())
                .setExpiration(Date.from(new Date().toInstant().plusSeconds(3600)))
                .claim("username",user.getUsername())
                .claim("firstname",user.getFirstName())
                .claim("lastname",user.getLastName())
                .signWith(
                        SignatureAlgorithm.HS256,secret.getBytes()
                )
                .compact();
    }
}
