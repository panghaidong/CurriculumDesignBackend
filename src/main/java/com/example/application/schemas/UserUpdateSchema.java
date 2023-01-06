package com.example.application.schemas;

import com.example.application.enums.UserPower;

public record UserUpdateSchema(String name, String email, String password, UserPower power) {
}
