package com.example.application.database.models;

import com.example.application.enums.UserPower;
import com.example.application.utils.Encrypt;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true,nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String passwd;
    @Column(nullable = false)
    private UserPower power;

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(final String password) {
        this.passwd = new Encrypt().MD5(this.id + password);
    }

    public UserPower getPower() {
        return this.power;
    }

    public void setPower(UserPower power) {
        this.power = power;
    }

    public void update(String name, String email, String password, UserPower power) {
        if (name != null) this.setName(name);
        if (email != null) this.setEmail(email);
        if (password != null) this.setPasswd(password);
        if (power != null) this.setPower(power);
    }

}