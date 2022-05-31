package ru.praktikum.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.stellarburgers.model.*;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {
    @Step("Send POST request to /auth/register")
    public ValidatableResponse registerNewUserAndReturnResponse(RegisterUserRequest registerUserRequest) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(registerUserRequest.toJsonString())
                .when()
                .post("auth/register")
                .then();
    }

    @Step("Send POST request to /auth/login")
    public ValidatableResponse loginUserAndReturnResponse(LoginUserRequest loginUserRequest) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(loginUserRequest.toJsonString())
                .when()
                .post("auth/login")
                .then();
    }

    @Step("Send PATCH request to /auth/user")
    public ValidatableResponse updateUserDataAndReturnResponse(UpdateUserRequest updateUserDataRequest, Boolean authorize) {
        if (authorize) {
            return given()
                    .spec(getBaseSpec())
                    .auth().oauth2(Tokens.getAccessToken())
                    .body(updateUserDataRequest.toJsonString())
                    .when()
                    .patch("auth/user")
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .body(updateUserDataRequest.toJsonString())
                    .when()
                    .patch("auth/user")
                    .then();
        }
    }

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


    @Step("Saving token for later")
    public void saveUserToken(ValidatableResponse response) {
        UserRegisterResponse parsedResponse = response
                .extract()
                .body()
                .as(UserRegisterResponse.class);

        if (parsedResponse != null) {
            Tokens.setAccessToken(parsedResponse.getAccessToken().substring(7));
        }
    }

    @Step("Send DELETE request to /auth/register")
    public void deleteUserAndFlushToken() {
        if (Tokens.getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .when()
                .delete("auth/user")
                .then()
                .statusCode(202);

        Tokens.flush();
    }
}
