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

//    private Location(Parcel in) {
//        id = in.readInt();
//        street = in.readString();
//        state = in.readString();
//        city = in.readString();
//        zipcode = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(street);
//        dest.writeString(state);
//        dest.writeString(city);
//        dest.writeString(zipcode);
//    }
//
//    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
//
//        @Override
//        public Location createFromParcel(Parcel source) {
//            return new Location(source);
//        }
//
//        @Override
//        public Location[] newArray(int size) {
//            return new Location[size];
//        }
//    };
}
