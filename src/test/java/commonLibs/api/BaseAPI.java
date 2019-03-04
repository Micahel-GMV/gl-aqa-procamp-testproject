package commonLibs.api;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;
import commonLibs.pojos.Contact;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static commonLibs.utils.ResponseExtractor.getContactsFromResponse;
import static io.restassured.RestAssured.given;

public class BaseAPI {

    private static final Logger log = LogManager.getLogger(BaseAPI.class);

    private static final String CONTACTS_ENDPOINT = "/api/v1/contacts";
    private static final String APPCONFIG_ENDPOINT = "/application.wadl";

    @Step
    public ValidatableResponse getApplicationWadl(){
        log.info("Getting application params.");
        ValidatableResponse validatableResponse;
        try {
            validatableResponse = given()
                    .when()
                    .get(APPCONFIG_ENDPOINT)
                    .then();
        }
        catch (Exception e){
            log.error( "Error doing application request", e);
            throw e;
        }
        return validatableResponse;
    }

    @Step
    public ValidatableResponse getContactsResponse(){
        log.info("Doing contacts request.");
        ValidatableResponse validatableResponse;
        try {
            validatableResponse = given()
                    .when()
                    .get(CONTACTS_ENDPOINT)
                    .then();
        }
        catch (Exception e){
            log.error( "Error doing get contacts request", e);
            throw e;
        }
        return validatableResponse;
    }

    @Step
    public List<Contact> getContacts() throws IOException {
        ValidatableResponse response = getContactsResponse().statusCode(200);
        return getContactsFromResponse(response.extract().body().asString());
    }

    @Step
    public ValidatableResponse addContact(Contact contact){
        log.info("Adding contact " + contact.getEmail());
        ValidatableResponse validatableResponse;
        try {
            validatableResponse = given()
                    .when()
                    .header("Content-Type", "application/json")
                    .body(contact.toJson())
                    .post(CONTACTS_ENDPOINT)
                    .then();
        }
        catch (Exception e){
            log.error( "Error doing add contact request", e);
            throw e;
        }
        return validatableResponse;
    }

    @Step
    public ValidatableResponse getContactResponse(int id){
        log.info("Getting contact by id=" + id);
        ValidatableResponse validatableResponse;
        try {
            validatableResponse = given()
                    .when()
                    .get(CONTACTS_ENDPOINT + "/" + id)
                    .then();
        }
        catch (Exception e){
            log.error( "Error doing get contact request", e);
            throw e;
        }
        return validatableResponse;
    }

    @Step
    public ValidatableResponse getContactResponse(String email, String firstName){
        log.info("Getting contact by creds " + firstName + ":" + email);
        ValidatableResponse validatableResponse;
        try {
            validatableResponse = given()
                    .when()
                    .param("email", email)
                    .param("firstName", firstName)
                    .get(CONTACTS_ENDPOINT)
                    .then();
        }
        catch (Exception e){
            log.error( "Error doing get contact request", e);
            throw e;
        }
        return validatableResponse;
    }
}
