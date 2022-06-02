package ru.praktikum.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.stellarburgers.client.OrderClient;
import ru.praktikum.stellarburgers.client.UserClient;
import ru.praktikum.stellarburgers.model.CreateOrderRequest;
import ru.praktikum.stellarburgers.model.GetCustomUsersOrders;
import ru.praktikum.stellarburgers.model.RegisterUserRequest;

import static org.hamcrest.Matchers.*;


@RunWith(Parameterized.class)
public class CreateCustomUserOrdersTest {
    UserClient userClient;
    OrderClient orderClient;
    RegisterUserRequest registerUserRequest;
    ValidatableResponse validatableResponse;
    GetCustomUsersOrders getCustomUsersOrders;

    private final Boolean authorize;
    private final Integer ordersCount;
    private final Integer returnedOrdersCount;
    private final Boolean success;
    private final Integer code;

    public CreateCustomUserOrdersTest(Boolean authorize, Integer ordersCount, Integer returnedOrdersCount, Boolean success, Integer code) {
        this.authorize = authorize;
        this.ordersCount = ordersCount;
        this.returnedOrdersCount = returnedOrdersCount;
        this.success = success;
        this.code = code;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {true, 0, 0, true, 200},
                {true, 1, 1, true, 200},
                {true, 50, 50, true, 200},
                {true, 49, 49, true, 200},
                {true, 51, 50, true, 200},
                {false, 1, 1, false, 401},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        registerUserRequest = RegisterUserRequest.getRandom();
        validatableResponse = userClient.registerNewUserAndReturnResponse(registerUserRequest);
        userClient.saveUserToken(validatableResponse);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"});
        for (int i = 0; i < ordersCount; i++){
            orderClient.createOrderAndReturnResponse(createOrderRequest, true);
        }
        getCustomUsersOrders = new  GetCustomUsersOrders(registerUserRequest.email);
    }

    @After
    public void tearDown() throws InterruptedException {
        userClient.deleteUserAndFlushToken();
        Thread.sleep(100);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    public void getCustomUserOrdersTest() {
        validatableResponse = orderClient.getSpecificUserOrdersAndReturnResponse(getCustomUsersOrders, authorize)
                .assertThat()
                .statusCode(code)
                .body("success", equalTo(this.success));

        if (authorize) {
            validatableResponse
                    .body("orders.size()", is(returnedOrdersCount));
        }
    }
}
