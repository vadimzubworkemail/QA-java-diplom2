package ru.praktikum.stellarburgers.model;

public class UpdateUserRequest extends Request {
    public final String email;
    public final String name;

    public UpdateUserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
