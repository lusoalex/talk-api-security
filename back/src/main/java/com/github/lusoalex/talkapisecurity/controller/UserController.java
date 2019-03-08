package com.github.lusoalex.talkapisecurity.controller;

import com.github.lusoalex.talkapisecurity.model.User;
import com.github.lusoalex.talkapisecurity.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api( description = "Access to Users resource.")
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create a new User.", response = User.class)
    @PostMapping
    public Mono<User> create(@RequestBody User user) {
        return userService.register(user);
    }

    @ApiOperation(value = "List all Users in the system.", response = User[].class)
    @GetMapping
    public Flux<User> list() {
        return userService.findlAll();
    }

    @ApiOperation(value = "Return a specific User according to its unique identifier.", response = User.class)
    @GetMapping("/{id}")
    public Mono<User> getById(@PathVariable("id") String id) {
        return userService.findById(id);
    }

    @ApiOperation(value = "Update a specific User according to its unique identifier.", response = User.class)
    @PutMapping("/{id}")
    public Mono<User>updateById(@PathVariable("id") String id, @RequestBody User user) {
        return Mono.just(user)
                .map(u -> u.withId(id))
                .flatMap(userService::update);
    }

    @ApiOperation(value = "Delete a specific User according to its unique identifier.", response = Void.class)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return userService.delete(id);
    }
}
