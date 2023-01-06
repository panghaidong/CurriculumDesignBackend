package com.example.application.schemas;

import com.example.application.enums.UserPower;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFilterSchema {
    private final String name;
    private final String email;
    private final List<UserPower> power;

    public UserFilterSchema(final String name, final String email, final UserPower power) {
        this.name = Objects.requireNonNullElse(name, "");
        this.email = Objects.requireNonNullElse(email, "");
        this.power = new ArrayList<>();
        if (power != null) {
            this.power.add(power);
        } else {
            this.power.addAll(List.of(UserPower.values()));
        }
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public List<UserPower> getPower() {
        return this.power;
    }

}
