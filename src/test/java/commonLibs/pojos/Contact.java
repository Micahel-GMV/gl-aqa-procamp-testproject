package commonLibs.pojos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commonLibs.utils.NameManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tests.api.contacts.ContactsTest;

import java.util.Objects;

public class Contact {
    private static final Logger log = LogManager.getLogger(Contact.class);
    private String email, firstName, lastName;
    private ObjectMapper objectMapper = new ObjectMapper();
    private int id = -1;

    public Contact(){
        NameManager nameManager = new NameManager();
        String prefix = nameManager.getStandardName();
        setContact(prefix + "@test.com", prefix + "fn", prefix + "ln");
    }

    public Contact(String postfix){
        NameManager nameManager = new NameManager();
        String prefix = nameManager.getStandardName();
        setContact(prefix + "@test.com", prefix + "fn" + postfix, prefix + "ln");
    }

    public Contact(String email, String firstName, String lastName){
        setContact(email, firstName, lastName);
    }

    public String toJson(){
        try {
            return  objectMapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e){
            log.error("Couldn`t generate json.");
            return null;
        }
    }

    private void setContact(String email, String firstName, String lastName){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;

        return Objects.equals(email, contact.email) &&
                Objects.equals(firstName, contact.firstName) &&
                Objects.equals(lastName, contact.lastName);
    }
}
