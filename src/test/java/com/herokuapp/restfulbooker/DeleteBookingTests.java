package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookingTests extends BaseTest {

    @Test
    public void deleteBookingTest() {
        Response responseCreate = createBooking();
        responseCreate.print();
        int bookingid = responseCreate.jsonPath().getInt("bookingid");

        Response responseDelete = RestAssured.given(spec).auth().preemptive().basic("admin", "password123").
                contentType(ContentType.JSON).delete("/booking/" + bookingid);
        responseDelete.print();

        Response responseGet = RestAssured.get("https://restful-booker.herokuapp.com/booking/" + bookingid);

       Assert.assertEquals(responseDelete.getStatusCode(), 201, "Status code should be 201, but it's not");
       Assert.assertEquals(responseGet.getBody().asString(), "Not Found", "Body should be 'Not Found', but it's not");
    }
}
