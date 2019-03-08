package com.github.lusoalex.talkapisecurity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@CrossOrigin
@Api( description = "Ping for availability")
@RestController
@RequestMapping("/ping")
public class PingController {

    @ApiOperation(value = "Return 200 status code if no problem...", response = String.class)
    @GetMapping
    public Mono<String> ping() {
        return Mono.just("pong");
    }
}
