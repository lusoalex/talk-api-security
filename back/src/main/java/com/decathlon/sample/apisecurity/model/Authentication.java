package com.decathlon.sample.apisecurity.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;

@ApiModel(description = "Token")
@Wither
@Data
@AllArgsConstructor
public class Authentication {

    private String access_token;
}
