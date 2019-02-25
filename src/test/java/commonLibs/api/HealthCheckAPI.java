package commonLibs.api;

import commonLibs.configReaders.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class HealthCheckAPI {
    private static final Logger log = LogManager.getLogger(HealthCheckAPI.class);
    private static final String HEALTHCHECK_ENDPOINT = "/healthcheck";

    public ValidatableResponse doHealthCheck(){
        log.info("Doing healthcheck request.");
        ValidatableResponse validatableResponse;
        try {
             validatableResponse = given()
                    .when()
                    .get(HEALTHCHECK_ENDPOINT)
                    .then();
        }
        catch (Exception e){
            log.error( "Error doing healthcheck request", e);
            throw e;
        }
        return validatableResponse;
    }
}
