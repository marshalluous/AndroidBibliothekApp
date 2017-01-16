package ch.dss.gadgeothek;

import java.io.Serializable;

public class UserRegistrationData implements Serializable {

    private String name;
    private boolean newsletter;

    public UserRegistrationData() {
    }

    public UserRegistrationData(String name, boolean newsletter) {
        this.name = name;
        this.newsletter = newsletter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNewsletter() {
        return newsletter;
    }

    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }
}
