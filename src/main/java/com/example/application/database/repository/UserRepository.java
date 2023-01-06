package com.example.application.database.repository;

import com.example.application.database.models.User;
import com.example.application.enums.UserPower;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByName(@NonNull String name);

    List<User> findByNameContainingAndEmailContainingAndPowerIn(String name, String email, List<UserPower> power);
}