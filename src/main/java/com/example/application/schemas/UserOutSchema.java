package com.example.application.schemas;

import com.example.application.enums.UserPower;

import java.util.UUID;

public record UserOutSchema(UUID id, String name, String email, UserPower power) {

}
