package ru.praktikum.stellarburgers.model;

public class CreateOrderRequest extends Request {
    public final String[] ingredients;

    public CreateOrderRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
