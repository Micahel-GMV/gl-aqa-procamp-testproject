package tests.api;

import commonLibs.api.HealthCheckAPI;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import tests.BaseTest;

import static org.hamcrest.core.StringContains.containsString;

public class HealthCheckTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(HealthCheckTest.class);
    private HealthCheckAPI healthCheckAPI = new HealthCheckAPI();

    @Test
    public void doHealthCheck(){

        log.info("Healthcheck of " + config.getUri() + " START.");

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("live"))
                .build();

        healthCheckAPI.doHealthCheck()
                    .spec(responseSpec);

        log.info("Healthcheck of " + config.getUri() + " SUCCESS.");
    }
}
