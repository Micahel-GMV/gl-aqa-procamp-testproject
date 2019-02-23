package tests.api;

import commonLibs.api.HealthCheckAPI;
import commonLibs.configReaders.ConfigReader;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.ConnectException;

import static org.hamcrest.core.StringContains.containsString;
import static org.testng.Assert.fail;

public class HealthCheckTest {

    private static final Logger log = LogManager.getLogger(HealthCheckTest.class);

    @Test
    public void doHealthCheck(){

        log.info("Healthcheck of " + ConfigReader.DEFAULT.getUri() + " START.");

        HealthCheckAPI healthCheckAPI = new HealthCheckAPI();

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("live"))
                .build();

        try {
            healthCheckAPI.doHealthCheck()
                    .then()
                    .spec(responseSpec);
            log.info("Healthcheck of " + ConfigReader.DEFAULT.getUri() + " SUCCESS.");
        }
        catch (Exception e)
        {
            log.error(e);
            fail("Could not connect to the service.");
        }
    }
}
