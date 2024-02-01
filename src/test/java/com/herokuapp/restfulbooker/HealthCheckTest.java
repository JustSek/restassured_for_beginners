package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.head;

public class HealthCheckTest extends BaseTest{

    @Test
    public void healthCheckTests() {
        given().
             spec(spec).
        when().
             get("/ping").
        then().
             assertThat().
             statusCode(201);
    }

    @Test
    public void headersAndCookiesTest() {
        Header some_header = new Header("some_header", "Some_value");
        spec.header(some_header);

        Cookie some_cookie = new Cookie.Builder("some_cookie", "some_cookie_value").build();
        spec.cookie(some_cookie);

        Response response = RestAssured.given(spec).
                cookie("Test cookie name", "Test cookie value").
                header("Test header name", "Test header value").log().all().get("/ping");

        Headers headers = response.getHeaders();
        System.out.println("Headers: " + headers);

        Header server_header_1 = headers.get("Server");
        System.out.println(server_header_1.getName() + ": " + server_header_1.getValue());

        String server_header_2 = response.getHeader("Server");
        System.out.println("Server: " + server_header_2);


        Cookies cookies = response.getDetailedCookies();
        System.out.println("Cookies: " + cookies);
    }
}
