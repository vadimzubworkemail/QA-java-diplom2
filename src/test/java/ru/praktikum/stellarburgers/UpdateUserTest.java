package ru.praktikum.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.stellarburgers.client.UserClient;
import ru.praktikum.stellarburgers.model.RegisterUserRequest;
import ru.praktikum.stellarburgers.model.UpdateUserRequest;


import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UpdateUserTest {
    UserClient userClient;
    RegisterUserRequest registerUserRequest;
    ValidatableResponse validatableResponse;

    private final Boolean authorize;
    UpdateUserRequest updateUserRequest;
    private final Boolean success;
    private final Integer code;

    public UpdateUserTest(Boolean authorize, UpdateUserRequest updateUserRequest, Boolean success, Integer code) {
        this.authorize = authorize;
        this.updateUserRequest = updateUserRequest;
        this.success = success;
        this.code = code;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {true, new UpdateUserRequest("pupkin@gmail.com", "Vasya"), true, 200},
                {true, new UpdateUserRequest("pupkin@gmail.com", null), true, 200},
                {true, new UpdateUserRequest(null, "Vasya"), true, 200},
                {false, new UpdateUserRequest("pupkin@gmail.com", "Vasya"), false, 401},
                {false, new UpdateUserRequest("pupkin@gmail.com", null), false, 401},
                {false, new UpdateUserRequest(null, "Vasya"), false, 401}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        registerUserRequest = RegisterUserRequest.getRandom();
        validatableResponse = userClient.registerNewUserAndReturnResponse(registerUserRequest);
        userClient.saveUserToken(validatableResponse);
    }

    @After
    public void tearDown() {
        userClient.deleteUserAndFlushToken();
    }

    @Test
    public void updateUserDataTest() {
        userClient.updateUserDataAndReturnResponse(updateUserRequest, authorize)
                .assertThat().body("success", equalTo(success))
                .and()
                .statusCode(code);
    }
}
