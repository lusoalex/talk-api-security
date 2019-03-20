package com.github.lusoalex.talkapisecurity.controller;

import com.github.lusoalex.talkapisecurity.model.Authentication;
import com.github.lusoalex.talkapisecurity.model.User;
import com.github.lusoalex.talkapisecurity.service.UserService;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.util.Base64;

@CrossOrigin
@Api( description = "Authenticate user demo (with vulnerabilities)")
@RestController
@RequestMapping("/authenticate")
public class LoginController {

    @Value("${provider:http://localhost:8092/confoo}")
    private String provider;

    @Value("${client_id:api-security-talk}")
    private String client_id;

    @Value("${client_secret:api-security-talk-secret}")
    private String client_secret;


    @Autowired
    private UserService userService;

    @ApiOperation(value = "User Basic Authentication", response = User.class)
    @PostMapping(value = "/basic", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "Oauth2 code callback endpoint", response = User.class)
    @PostMapping(value = "/oauth2/code", produces = MediaType.APPLICATION_JSON_VALUE)
    public Publisher<String> code(@RequestParam String code, @RequestParam String state) throws SSLException {

        String credentials = new String(Base64.getEncoder().encode((client_id+":"+client_secret).getBytes()));

        MultiValueMap<String,String> map = new LinkedMultiValueMap();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("state",state);
        map.add("redirect_uri","http://localhost:8081/login");
        map.add("scope","openid");

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)//only for demo...
                .build();

        return WebClient
                //create request
                .builder()
                .clientConnector(new ReactorClientHttpConnector(opt -> opt.sslContext(sslContext)))
                .baseUrl(provider)
                .filter(ExchangeFilterFunctions.basicAuthentication(client_id, client_secret))
                .build()
                //request parameters
                .post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept( MediaType.APPLICATION_JSON )
                .header("Authorization","Basic "+credentials)
                .body(BodyInserters.fromFormData(map))
                //call request
                .retrieve()
                .bodyToMono(String.class);
    }
}

