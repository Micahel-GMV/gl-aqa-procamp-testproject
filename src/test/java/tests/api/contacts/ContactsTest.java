package tests.api.contacts;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import commonLibs.api.BaseAPI;
import commonLibs.pojos.Contact;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import tests.BaseTest;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static commonLibs.utils.ResponseExtractor.getContactsFromResponse;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.testng.Assert.*;

public class ContactsTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(ContactsTest.class);
    private BaseAPI baseAPI = new BaseAPI();

    @Test
    public void contactsList(){
        log.info("Checking contacts list START.");

        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(
                        ValidationConfiguration.newBuilder()
                                .setDefaultVersion(SchemaVersion.DRAFTV4).freeze())
                .freeze();

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(matchesJsonSchema(config.getConfigFileAsString("schemas/contacts.json"))
                                .using(jsonSchemaFactory))
                .expectBody("status", equalTo(200))
                .build();

        baseAPI.getContactsResponse()
                .spec(responseSpec);

        log.info("Checking contacts list SUCCESS.");
    }

    @Test
    public void addContact(){
        log.info("Add contact test START.");
        Contact contact = new Contact();
        ValidatableResponse response = baseAPI.addContact(contact);
        List<Contact> addedContactList = null;
        assertEquals(response.extract().statusCode(), 201, "Wrong response status code on adding a contact.");
        log.info("Checking response on adding contact.");
        try {
            addedContactList = getContactsFromResponse(response.extract().body().asString());
        }
        catch (IOException e){
            fail("Couldn`t parse response on adding new contact.");
        }

        assertEquals(addedContactList.size(), 1, "Wrong contacts count in response on registering new contact.");
        Contact addedContact = addedContactList.get(0);

        assertEquals(contact, addedContact, "Wrong contact in response after adding a contact.");

        log.info("Checking contacts list after adding a contact.");
        List<Contact> contacts = null;
        try {
            contacts = baseAPI.getContacts();
            assertTrue(contacts.contains(contact), "Contact didn`t appear in contacts list after adding it.");
        } catch (IOException e) {
            fail("Couldn`t get contacts list.");
        }
        List<Integer> ids = contacts.stream().map(c -> c.getId()).collect(Collectors.toList());
        HashSet<Integer> idsSet = new HashSet<>(ids);
        assertEquals(idsSet.size(), ids.size(), "There is equivalent ids in id`s list.");

        log.info("Add contact test SUCCESS.");
    }

    @Test//Looks like a bug. Duplicates must not be allowed.
    public void duplicateContact() throws IOException {
        log.info("Duplicate contact test START.");
        Contact contact = new Contact();
        baseAPI.addContact(contact);
        ValidatableResponse response = baseAPI.addContact(contact);
        assertFalse(baseAPI.getContacts().contains(contact), "Duplicate contact added.");
        assertNotEquals(response.extract().statusCode(), 201, "");
        log.info("Duplicate contact test SUCCESS.");
    }

    @Test
    public void getContactById() throws IOException {
        log.info("Get contact by id test START.");
        Contact contact = new Contact();
        log.info("Adding contact and getting it`s id.");
        int id = getContactsFromResponse(baseAPI.addContact(contact).extract().body().asString()).get(0).getId();
        contact.setId(id);

        log.info("Getting contact by id and ensuring it`s single and it`s correct one.");
        List<Contact> contacts = getContactsFromResponse(baseAPI.getContactResponse(id).extract().body().asString());
        assertEquals(contacts.size(), 1, "Not single contact arrived in contact request by id.");
        assertEquals(contacts.get(0), contact, "Wrong contact received in get by id request.");
        assertEquals(contacts.get(0).getId(), contact.getId(), "Wrong ID in response.");
        log.info("Get contact by id test SUCCESS.");
    }

    @Test
    public void getContactByWrongId() throws IOException {
        log.info("Get contact by wrong id test START.");
        ValidatableResponse response = baseAPI.getContactResponse(Integer.MAX_VALUE);//In real life I`ll go to devs and ask about
                                                                                // generating id rules to secure id doesn`t exsist.
        response.statusCode(404);
        log.info("Get contact by wrong id test SUCCESS.");
    }

    @Test
    public void getContactByCreds() throws IOException {//Looks like it`s regEx in endpoint description. Need to play with
                                                        // it and implement some search tests.
        log.info("Get contact by creds test START.");
        Contact contact = new Contact();
        log.info("Adding contact and getting it`s id.");
        int id = getContactsFromResponse(baseAPI.addContact(contact).extract().body().asString()).get(0).getId();
        contact.setId(id);

        log.info("Getting contact by id and ensuring it`s single and it`s correct one.");
        List<Contact> contacts = getContactsFromResponse(baseAPI.getContactResponse(contact.getEmail(), contact.getFirstName()).extract().body().asString());
        assertEquals(contacts.size(), 1, "Not single contact arrived in contact request by id.");
        assertEquals(contacts.get(0), contact, "Wrong contact received in get by id request.");
        assertEquals(contacts.get(0).getId(), contact.getId(), "Wrong ID in response.");
        log.info("Get contact by creds test SUCCESS.");
    }

    @Test
    public void getContactByWrongCreds() throws IOException {
        log.info("Get contact by wrong creds test START.");
        Contact contact = new Contact("wrong");

        log.info("Getting contact by id and ensuring it`s single and it`s correct one.");
        List<Contact> contacts = getContactsFromResponse(baseAPI.getContactResponse(contact.getEmail(), contact.getFirstName()).extract().body().asString());
        assertEquals(contacts.size(), 0, "Some contacts received while trying to get not existent contact.");
        log.info("Get contact by wrong creds test SUCCESS.");
    }
}
