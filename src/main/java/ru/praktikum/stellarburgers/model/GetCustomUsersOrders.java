package ru.praktikum.stellarburgers.model;

public class GetCustomUsersOrders extends Request {
    public final String email;

    public GetCustomUsersOrders(String email) {
        this.email = email;
    }
}
