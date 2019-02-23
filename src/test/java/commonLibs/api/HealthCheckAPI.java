package commonLibs.api;

import commonLibs.configReaders.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class HealthCheckAPI {
    private static final Logger log = LogManager.getLogger(HealthCheckAPI.class);
    private static final String HEALTHCHECK_ENDPOINT = "/healthcheck";

    public Response doHealthCheck(){
        return given()
                .when()
                .get(HEALTHCHECK_ENDPOINT);
    }
}
