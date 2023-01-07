package com.example.application.apis;

import com.example.application.database.models.UserModel;
import com.example.application.database.repository.UserRepository;
import com.example.application.enums.UserEnum.UserPowerEnum;
import com.example.application.schemas.UserSchema.UserCreateSchema;
import com.example.application.schemas.UserSchema.UserFilterSchema;
import com.example.application.schemas.UserSchema.UserOutSchema;
import com.example.application.schemas.UserSchema.UserUpdateSchema;
import com.example.application.utils.Model2Schema;
import com.example.application.utils.Verify;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
public class UserController {
    private final UserRepository userRepository;

    // Inject UserRepository
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users after filter
    @Operation(summary = "Get users")
    @Tag(name = "User")
    @GetMapping
    public List<UserOutSchema> getUsers(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) UserPowerEnum power) {
        // TODO: Add pagination
        // TODO: Add sort
        UserFilterSchema userFilterSchema = new UserFilterSchema(name, email, power);
        // Get all users after filter by filter schema
        List<UserModel> users = userRepository.findByNameContainingAndEmailContainingAndPowerIn(userFilterSchema.getName(),
                userFilterSchema.getEmail(),
                userFilterSchema.getPower());
        // Convert UserModel to UserOutSchema
        return users.stream().map(Model2Schema::user2UserOutSchema).toList();
    }

    // Create a new user
    @Operation(summary = "Create user")
    @Tag(name = "User")
    @PutMapping
    public UserOutSchema createUser(@RequestBody UserCreateSchema form,
                                    HttpServletResponse response) {
        UserModel user = new UserModel();
        user.update(form.name(), form.email(), form.password(), form.power());
        UserOutSchema result = null;
        try {
            // Save user to database
            final UserModel saveResult = userRepository.save(user);
            // Convert UserModel to UserOutSchema
            result = Model2Schema.user2UserOutSchema(saveResult);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return result;
    }

    // Get a user by id
    @Operation(summary = "Get user by id")
    @Tag(name = "User")
    @GetMapping("/{userId}")
    public UserOutSchema getUser(@PathVariable String userId,
                                 HttpServletResponse response) {
        // Get user instance by user id
        UserModel user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // User not found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        // Convert UserModel to UserOutSchema
        return Model2Schema.user2UserOutSchema(user);
    }

    // Update a user by id
    @Operation(summary = "Update user by id")
    @Tag(name = "User")
    @PatchMapping("/{userId}")
    public UserOutSchema updateUser(@PathVariable String userId,
                                    @RequestBody UserUpdateSchema form,
                                    HttpServletResponse response,
                                    HttpSession session) {
        // Get user instance by user id
        UserModel user = userRepository.findById(userId).orElse(null);
        UserOutSchema result = null;
        if (!Verify.userPowerVerify(session, UUID.fromString(userId))) {
            // User not login or user permission denied
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        if (user == null) {
            // User not found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            // Update user
            user.update(form.name(), form.email(), form.password(), form.power());
            try {
                // Save user to database
                final UserModel saveResult = userRepository.save(user);
                result = Model2Schema.user2UserOutSchema(saveResult);
            } catch (Exception e) {
                // Update failed
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }
        return result;
    }

    // Delete a user by id
    @Operation(summary = "Delete user by id")
    @Tag(name = "User")
    @DeleteMapping("/{userId}")
    public UserOutSchema deleteUser(@PathVariable String userId, HttpServletResponse response, HttpSession session) {
        if (Verify.userPowerVerify(session, UUID.fromString(userId))) {
            // User not login or user permission denied
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            // Get user instance by user id
            UserModel user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                // User not found
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                // Delete user
                userRepository.delete(user);
                // Convert UserModel to UserOutSchema
                return Model2Schema.user2UserOutSchema(user);
            }
        }
    }
}
