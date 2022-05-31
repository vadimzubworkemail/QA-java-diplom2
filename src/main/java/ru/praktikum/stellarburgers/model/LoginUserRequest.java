package ru.praktikum.stellarburgers.model;

public class LoginUserRequest extends Request {
    public final String email;
    public final String password;

    public LoginUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
