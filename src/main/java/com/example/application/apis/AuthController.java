package com.example.application.apis;

import com.example.application.database.models.UserModel;
import com.example.application.database.repository.UserRepository;
import com.example.application.schemas.AuthSchema.AuthLoginSchema;
import com.example.application.schemas.UserSchema.UserOutSchema;
import com.example.application.utils.Model2Schema;
import com.example.application.utils.Verify;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/auth", produces = "application/json")
public class AuthController {
    private final UserRepository userRepository;
    @Value("${application.session-duration}")
    private Integer sessionDuration;

    // Inject UserRepository
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Login
    @Operation(summary = "Login")
    @Tag(name = "Auth")
    @PostMapping
    public UserOutSchema login(@RequestBody AuthLoginSchema form, HttpServletResponse response, HttpSession session) {
        // Get user by name
        UserModel user = userRepository.findByName(form.name()).orElse(null);
        UserOutSchema result;
        if (user == null) {
            // cancel session
            session.invalidate();
            // User not found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            result = null;
        } else {
            if (Verify.userPasswordVerify(form, user)) {
                // Set session
                session.setAttribute("id", user.getId());
                session.setAttribute("name", user.getName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("power", user.getPower());
                session.setMaxInactiveInterval(sessionDuration);
                result = Model2Schema.user2UserOutSchema(user);
            } else {
                // Password incorrect
                session.invalidate();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                result = null;
            }

        }
        return result;
    }

    // Logout
    @Operation(summary = "Logout")
    @Tag(name = "Auth")
    @DeleteMapping
    public void logout(HttpSession session) {
        session.invalidate();
    }

}
