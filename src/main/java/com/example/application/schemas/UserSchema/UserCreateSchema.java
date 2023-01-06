package com.example.application.schemas.UserSchema;

import com.example.application.enums.UserEnum.UserPowerEnum;

public record UserCreateSchema(String name, String email, String password, UserPowerEnum power) {
}
