package com.example.application.schemas.UserSchema;

import com.example.application.enums.UserEnum.UserPowerEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFilterSchema {
    private final String name;
    private final String email;
    private final List<UserPowerEnum> power;

    public UserFilterSchema(final String name, final String email, final UserPowerEnum power) {
        this.name = Objects.requireNonNullElse(name, "");
        this.email = Objects.requireNonNullElse(email, "");
        this.power = new ArrayList<>();
        if (power != null) {
            this.power.add(power);
        } else {
            this.power.addAll(List.of(UserPowerEnum.values()));
        }
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public List<UserPowerEnum> getPower() {
        return this.power;
    }

}
