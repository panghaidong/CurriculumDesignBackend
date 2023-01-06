package com.example.application.utils;

import com.example.application.database.models.User;
import com.example.application.enums.UserPower;
import com.example.application.schemas.UserOutSchema;

import java.util.UUID;

public class Model2Schema {
    public static UserOutSchema user2UserOutSchema(User user) {
        final UUID id = user.getId();
        final String name = user.getName();
        final String email = user.getEmail();
        final UserPower power = user.getPower();
        return new UserOutSchema(id, name, email, power);
    }
}
