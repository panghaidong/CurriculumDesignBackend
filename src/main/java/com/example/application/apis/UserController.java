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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public List<UserOutSchema> getUsers(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) UserPowerEnum power) {

        UserFilterSchema userFilterSchema = new UserFilterSchema(name, email, power);

        List<UserModel> users = userRepository.findByNameContainingAndEmailContainingAndPowerIn(userFilterSchema.getName(),
                userFilterSchema.getEmail(),
                userFilterSchema.getPower());
        return users.stream().map(Model2Schema::user2UserOutSchema).toList();
    }

    @PutMapping("/")
    public UserOutSchema createUser(@RequestBody UserCreateSchema form,
                                    HttpServletResponse response) {
        UserModel user = new UserModel();
        user.update(form.name(), form.email(), form.password(), form.power());
        UserOutSchema result = null;
        try {
            final UserModel saveResult = userRepository.save(user);
            result = Model2Schema.user2UserOutSchema(saveResult);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return result;
    }

    @GetMapping("/{userId}")
    public UserOutSchema getUser(@PathVariable String userId,
                                 HttpServletResponse response) {
        UserModel user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return Model2Schema.user2UserOutSchema(user);
    }

    @PatchMapping("/{userId}")
    public UserOutSchema updateUser(@PathVariable String userId,
                                    @RequestBody UserUpdateSchema form,
                                    HttpServletResponse response,
                                    HttpSession session) {
        UserModel user = userRepository.findById(userId).orElse(null);
        UserOutSchema result = null;
        if (!Verify.userPowerVerify(session, userId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            user.update(form.name(), form.email(), form.password(), form.power());
            try {
                userRepository.save(user);
                result = Model2Schema.user2UserOutSchema(user);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }
        return result;
    }

    @DeleteMapping("/{userId}")
    public UserOutSchema deleteUser(@PathVariable String userId, HttpServletResponse response, HttpSession session) {
        if (Verify.userPowerVerify(session, userId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            UserModel user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                userRepository.deleteById(userId);
                return Model2Schema.user2UserOutSchema(user);
            }
        }
    }
}
