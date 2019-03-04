package commonLibs.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commonLibs.pojos.Contact;
import io.restassured.response.ValidatableResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class ResponseExtractor {

    private static final Logger log = LogManager.getLogger(ResponseExtractor.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<Contact> getContactsFromResponse(String response) throws IOException {
        List<Contact> contacts = new LinkedList<>();
        try {
            JsonNode fullResponse = objectMapper.readTree(response);
            fullResponse.get("data")
                    .elements().forEachRemaining(element -> {
                JsonNode info = element.get("info");
                Contact contact = new Contact(info.get("email").asText(), info.get("firstName").asText(), info.get("lastName").asText());
                contact.setId(element.get("id").asInt());
                contacts.add(contact);
            });
        }
        catch (Exception e){
            log.error("Couldn`t extract contacts from json string. String is:" + response, e);
            throw e;
        }
        return contacts;
    }
}
