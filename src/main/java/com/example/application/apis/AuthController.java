package com.example.application.apis;

import com.example.application.database.models.UserModel;
import com.example.application.database.repository.UserRepository;
import com.example.application.schemas.AuthSchema.AuthLoginSchema;
import com.example.application.utils.Verify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    @Value("${application.session-duration}")
    private Integer sessionDuration;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public void login(@RequestBody AuthLoginSchema form, HttpServletResponse response, HttpSession session) {
        UserModel user = userRepository.findByName(form.name()).orElse(null);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            session.invalidate();
        } else {
            if (Verify.userPasswordVerify(form, user)) {
                session.setAttribute("id", user.getId());
                session.setAttribute("name", user.getName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("power", user.getPower());
                session.setMaxInactiveInterval(sessionDuration);
            } else {
                session.invalidate();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        }
    }

    @DeleteMapping("/")
    public void logout(HttpSession session) {
        session.invalidate();
    }

}
