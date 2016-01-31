package kidouchi.chronobook.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iuy407 on 11/19/15.
 */
public class Location extends RealmObject {

    @PrimaryKey
    private int id;

    private String street;
    private String state;
    private String city;
    private String zipcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
