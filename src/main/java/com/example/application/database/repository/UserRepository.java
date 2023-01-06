package com.example.application.database.repository;

import com.example.application.database.models.UserModel;
import com.example.application.enums.UserEnum.UserPowerEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserModel, String> {
    Optional<UserModel> findByName(@NonNull String name);

    List<UserModel> findByNameContainingAndEmailContainingAndPowerIn(String name, String email, List<UserPowerEnum> power);
}