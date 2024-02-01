package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class GetBookingIdsTest extends BaseTest {

    @Test
    public void getBookingIdsWithoutFilterTests() {
        Response response = RestAssured.given(spec).get("/booking");
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of bookings ids is empty, but it shouldn't be");
    }

    @Test
    public void getBookingIdsWithFilterTests() {
        spec.queryParam("firstname", "Mark");
        spec.queryParam("lastname", "Wilson");

        Response response = RestAssured.given(spec).get("/booking");
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of bookings ids is empty, but it shouldn't be");
    }
}
