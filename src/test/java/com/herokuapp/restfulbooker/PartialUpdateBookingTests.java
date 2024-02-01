package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PartialUpdateBookingTests extends BaseTest {

    @Test
    public void partialUpdateBookingTest() {
        // Create booking
        Response responseCreate = createBooking();
        responseCreate.print();
        // Get booking id of new booking
        int bookingid = responseCreate.jsonPath().getInt("bookingid");

        JSONObject patchedBody = new JSONObject();
        patchedBody.put("firstname", "Andrew");
        JSONObject patchedBookingDates = new JSONObject();
        patchedBookingDates.put("checkin", "2023-08-01");
        patchedBookingDates.put("checkout", "2023-10-04");
        patchedBody.put("bookingdates", patchedBookingDates);

        Response patchedResponse = RestAssured.given(spec).auth().preemptive().basic("admin", "password123").
                contentType(ContentType.JSON).
                body(patchedBody.toString()).patch("/booking/" + bookingid);

        patchedResponse.print();

        // Veryfication
        Assert.assertEquals(patchedResponse.getStatusCode(), 200, "Status code should be 200, but it's not");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(patchedResponse.jsonPath().getString("firstname"), "Andrew", "firstname in response is not expected");
        softAssert.assertEquals(patchedResponse.jsonPath().getString("lastname"), "Kowalski", "lastname in response is not expected");
        softAssert.assertEquals(patchedResponse.jsonPath().getInt("totalprice"), 150, "totalprice in response is not expected");
        softAssert.assertEquals(patchedResponse.jsonPath().getBoolean("depositpaid"), false, "depositpaid should be false, but it's not");

        // !!!  DEFECT FOUND: checkout or checkin date is changing to 0NaN-aN-aN when only one date is changed. If checkin is changed only then checkout is changed to 0NaN-aN-aN and contrariwise.
        softAssert.assertEquals(patchedResponse.jsonPath().getString("bookingdates.checkin"), "2023-08-01", "checkin in response is not expected");
        softAssert.assertEquals(patchedResponse.jsonPath().getString("bookingdates.checkout"), "2023-10-04", "checkout in response is not expected");

        softAssert.assertEquals(patchedResponse.jsonPath().getString("additionalneeds"), "Wine", "Additional needs in response is not expected");

        softAssert.assertAll();
    }

}
