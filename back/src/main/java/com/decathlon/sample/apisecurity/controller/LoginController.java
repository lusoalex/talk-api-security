package com.decathlon.sample.apisecurity.controller;

import com.decathlon.sample.apisecurity.model.Authentication;
import com.decathlon.sample.apisecurity.model.User;
import com.decathlon.sample.apisecurity.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Base64;

@CrossOrigin
@Api( description = "Authenticate user demo (with vulnerabilities)")
@RestController
@RequestMapping("/authenticate")
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "User Basic Authentication", response = User.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Publisher<Authentication> authenticate(@RequestHeader("Authorization") String authorization) {
        return Mono.just(authorization)
                .map(header -> header.replaceAll("Basic ",""))
                .map(Base64.getDecoder()::decode)
                .map(String::new)
                .map(s -> s.split(":"))
                .flatMap(credentials -> userService.authenticate(credentials[0], credentials[1]))
                .filter(users -> users.size()>0)
                .map(list -> list.get(0))
                .map(userService::buildJwt)
                .map(Authentication::new);
    }
}
