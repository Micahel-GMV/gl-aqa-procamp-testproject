package tests.api;

import commonLibs.api.BaseAPI;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import tests.BaseTest;

import static io.restassured.matcher.RestAssuredMatchers.matchesXsd;

public class AppConfigTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(AppConfigTest.class);
    private BaseAPI baseAPI = new BaseAPI();

    @Test
    public void checkAppConfig(){
        log.info("Checking application config START.");

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(matchesXsd(config.getConfigFileAsString("schemas/application.xsd")))
                .build();
        baseAPI.getApplicationWadl()
                .spec(responseSpec);
        log.info("Checking application config SUCCESS.");
    }
}
