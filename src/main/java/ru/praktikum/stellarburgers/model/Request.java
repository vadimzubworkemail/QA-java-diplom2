package ru.praktikum.stellarburgers.model;


public class Request {
    public static final String EMAIL_POSTFIX = "@yandex.ru";

    public String toJsonString() {
        return Utils.serializeToJsonIgnoreNulls(this);
    }
}
