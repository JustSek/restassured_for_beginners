package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class GetBookingTests extends BaseTest{

    @Test
    public void getBookingTests() {
        Response responseCreate = createBooking();
        responseCreate.print();

        spec.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));

        Response response = RestAssured.given(spec).get("/booking/{bookingId}");
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.jsonPath().getString("firstname"), "Alex", "firstname in response is not expected");
        softAssert.assertEquals(response.jsonPath().getString("lastname"), "Kowalski", "lastname in response is not expected");
        softAssert.assertEquals(response.jsonPath().getInt("totalprice"), 150, "totalprice in response is not expected");
        softAssert.assertFalse(response.jsonPath().getBoolean("depositpaid"), "depositpaid should be FALSE, but it's not");
        softAssert.assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2023-09-01", "checkin in response is not expected");
        softAssert.assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2023-09-04", "checkout in response is not expected");
        softAssert.assertEquals(response.jsonPath().getString("additionalneeds"), "Wine", "additionalneeds in response is not expected");
        softAssert.assertAll();
    }

    @Test
    public void getBookingXMLTests() {
        Response responseCreate = createBooking();
        responseCreate.print();

        spec.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));

        Header xml = new Header("Accept", "application/xml");
        spec.header(xml);
        Response response = RestAssured.given(spec).get("/booking/{bookingId}");
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.xmlPath().getString("booking.firstname"), "Alex", "firstname in response is not expected");
        softAssert.assertEquals(response.xmlPath().getString("booking.lastname"), "Kowalski", "lastname in response is not expected");
        softAssert.assertEquals(response.xmlPath().getInt("booking.totalprice"), 150, "totalprice in response is not expected");
        softAssert.assertFalse(response.xmlPath().getBoolean("booking.depositpaid"), "depositpaid should be FALSE, but it's not");
        softAssert.assertEquals(response.xmlPath().getString("booking.bookingdates.checkin"), "2023-09-01", "checkin in response is not expected");
        softAssert.assertEquals(response.xmlPath().getString("booking.bookingdates.checkout"), "2023-09-04", "checkout in response is not expected");
        softAssert.assertAll();
    }
}
