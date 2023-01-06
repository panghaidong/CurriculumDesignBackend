package com.example.application.utils;

import com.example.application.database.models.UserModel;
import com.example.application.enums.UserEnum.UserPowerEnum;
import com.example.application.schemas.UserSchema.UserOutSchema;

import java.util.UUID;

public class Model2Schema {
    public static UserOutSchema user2UserOutSchema(UserModel user) {
        final UUID id = user.getId();
        final String name = user.getName();
        final String email = user.getEmail();
        final UserPowerEnum power = user.getPower();
        return new UserOutSchema(id, name, email, power);
    }
}
