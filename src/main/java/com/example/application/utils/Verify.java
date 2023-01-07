package com.example.application.utils;

import com.example.application.database.models.UserModel;
import com.example.application.schemas.AuthSchema.AuthLoginSchema;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;

public class Verify {
    public static Boolean userPasswordVerify(AuthLoginSchema input, UserModel record) {
        String inputPassword = input.password();
        String inputPasswd = new Encrypt().MD5(inputPassword);
        String realPasswd = record.getPasswd();
        return Objects.equals(inputPasswd, realPasswd);
    }

    public static Boolean userPowerVerify(HttpSession session, UUID userId) {
        if (Integer.parseInt(session.getAttribute("power").toString()) == 0) {
            return true;
        } else {
            return Objects.equals(session.getAttribute("id"), userId);
        }
    }
}
