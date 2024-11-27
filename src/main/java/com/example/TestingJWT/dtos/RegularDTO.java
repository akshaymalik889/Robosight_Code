package com.example.TestingJWT.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularDTO {

    private Long id;
    private UserDto user;
}
