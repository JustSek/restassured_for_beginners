package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CreateBookingTests extends BaseTest {

    //@Test
    public void createBookingTest() {
        Response response = createBooking();
        response.print();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.jsonPath().getString("booking.firstname"), "Alex", "firstname in response is not expected");
        softAssert.assertEquals(response.jsonPath().getString("booking.lastname"), "Kowalski", "lastname in response is not expected");
        softAssert.assertEquals(response.jsonPath().getInt("booking.totalprice"), 150, "totalprice in response is not expected");
        softAssert.assertFalse(response.jsonPath().getBoolean("booking.depositpaid"), "depositpaid should be false, but it's not");
        softAssert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"), "2023-09-01", "checkin in response is not expected");
        softAssert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"), "2023-09-04", "checkout in response is not expected");
        softAssert.assertEquals(response.jsonPath().getString("booking.additionalneeds"), "Wine", "Additional needs in response is not expected");
        softAssert.assertAll();
    }

    @Test
    public void createBookingWithPOJOTest() {
        Bookingdates bookingdates = new Bookingdates("2023-09-25", "2023-09-27");
        Booking body = new Booking("Holly", "Molly", 200, false, bookingdates,"Wine" );

        Response response = RestAssured.given(spec).contentType(ContentType.JSON).body(body).post("/booking");
        response.print();
        Bookingid bookingid = response.as(Bookingid.class);

        System.out.println("Request booking : " + body.toString());
        System.out.println("Response booking : " + bookingid.getBooking().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        // Verify all fileds
        Assert.assertEquals(bookingid.getBooking().toString(),body.toString());

    }


}
