package ru.praktikum.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.stellarburgers.model.CreateOrderRequest;
import ru.praktikum.stellarburgers.model.GetCustomUsersOrders;
import ru.praktikum.stellarburgers.model.Tokens;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    @Step("Send GET request to /ingredients")
    public ValidatableResponse getIngredientsAndReturnResponse(Boolean authorize) {
        if (authorize) {
            return given()
                    .spec(getBaseSpec())
                    .auth().oauth2(Tokens.getAccessToken())
                    .when()
                    .get("ingredients")
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .get("ingredients")
                    .then();
        }
    }

    @Step("Send POST request to /orders")
    public ValidatableResponse createOrderAndReturnResponse(CreateOrderRequest createOrderRequest, Boolean authorize) {
        if (authorize) {
            return given()
                    .spec(getBaseSpec())
                    .auth().oauth2(Tokens.getAccessToken())
                    .body(createOrderRequest.toJsonString())
                    .when()
                    .post("orders")
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .body(createOrderRequest.toJsonString())
                    .when()
                    .post("orders")
                    .then();
        }
    }

    @Step("Send GET request to /orders")
    public ValidatableResponse getSpecificUserOrdersAndReturnResponse(GetCustomUsersOrders getSpecificUserOrdersRequest, Boolean authorize) {
        if (authorize) {
            return given()
                    .spec(getBaseSpec())
                    .auth().oauth2(Tokens.getAccessToken())
                    .body(getSpecificUserOrdersRequest.toJsonString())
                    .when()
                    .get("orders")
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .body(getSpecificUserOrdersRequest.toJsonString())
                    .when()
                    .get("orders")
                    .then();
        }
    }
}
