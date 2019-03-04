package com.decathlon.sample.apisecurity.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@ApiModel(description = "User representation only used as demo.")
@Entity
@Wither
@Data
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @ApiModelProperty(hidden = true, notes = "Unique identifier of the user.", example = "0a74c71b-fd98-4c99-b4c7-1bfd981c9912")
    private String id;
    @ApiModelProperty(notes = "First name of the user.", example = "John", required = true, position = 1)
    private String firstName;
    @ApiModelProperty(notes = "Last name of the user.", example = "Doe", required = true, position = 2)
    private String lastName;
    @Column(unique=true)
    @ApiModelProperty(notes = "username/login of the user.", example = "rand0mlY", required = true, position = 3)
    private String username;
    @ApiModelProperty(notes = "password of the user.", example = "rand0mlY", required = true, position = 4)
    private String password;

    public User() {
    }

    //standard setters and getters
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
