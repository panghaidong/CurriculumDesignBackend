package com.example.application.schemas.UserSchema;

import com.example.application.enums.UserEnum.UserPowerEnum;

import java.util.UUID;

public record UserOutSchema(UUID id, String name, String email, UserPowerEnum power) {

}
