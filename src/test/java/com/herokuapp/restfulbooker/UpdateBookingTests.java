package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdateBookingTests extends BaseTest {

    @Test
    public void updateBookingTest() {
        Response responseCreate = createBooking();
        responseCreate.print();

        int bookingid = responseCreate.jsonPath().getInt("bookingid");

        JSONObject body = new JSONObject();
        body.put("firstname", "Karen");
        body.put("lastname", "Kowalska");
        body.put("totalprice", 125);
        body.put("depositpaid", true);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2023-09-01");
        bookingdates.put("checkout", "2023-09-04");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "Two wines");

        Response responseUpdate = RestAssured.given(spec).auth().preemptive().basic("admin", "password123").
                contentType(ContentType.JSON).
                body(body.toString()).put("/booking/" + bookingid);
        responseUpdate.print();

       Assert.assertEquals(responseUpdate.getStatusCode(), 200, "Status code should be 200, but it's not");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(responseUpdate.jsonPath().getString("firstname"), "Karen", "firstname in response is not expected");
        softAssert.assertEquals(responseUpdate.jsonPath().getString("lastname"), "Kowalska", "lastname in response is not expected");
        softAssert.assertEquals(responseUpdate.jsonPath().getInt("totalprice"), 125, "totalprice in response is not expected");
        softAssert.assertTrue(responseUpdate.jsonPath().getBoolean("depositpaid"), "depositpaid should be true, but it's not");
        softAssert.assertEquals(responseUpdate.jsonPath().getString("bookingdates.checkin"), "2023-09-01", "checkin in response is not expected");
        softAssert.assertEquals(responseUpdate.jsonPath().getString("bookingdates.checkout"), "2023-09-04", "checkout in response is not expected");
        softAssert.assertEquals(responseUpdate.jsonPath().getString("additionalneeds"), "Two wines", "Additional needs in response is not expected");

        softAssert.assertAll();
    }
}
