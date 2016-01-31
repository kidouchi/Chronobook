package kidouchi.chronobook.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iuy407 on 11/21/15.
 */
public class Contact extends RealmObject {

    @PrimaryKey
    private int id;

    private String name;
    private String phoneNumber;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
