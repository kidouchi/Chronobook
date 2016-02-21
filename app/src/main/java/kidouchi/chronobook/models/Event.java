package kidouchi.chronobook.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iuy407 on 11/19/15.
 */
public class Event extends RealmObject {

    @PrimaryKey
    private int id;

    private String title;
    private String placeHolderFilepath;
    private String description;
    private long startDateTime;
    private long endDateTime;
    private String location;
    //    private Category category;
    private int categoryDrawable;
//    private Contact contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaceHolderFilepath() {
        return placeHolderFilepath;
    }

    public void setPlaceHolderFilepath(String placeHolderFilepath) {
        this.placeHolderFilepath = placeHolderFilepath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
        this.endDateTime = endDateTime;
    }

    public long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    public Category getCategory() {
//        return category;
//    }
//
//    public void setCategory(Category category) {
//        this.category = category;
//    }


    public int getCategoryDrawable() {
        return categoryDrawable;
    }

    public void setCategoryDrawable(int categoryDrawable) {
        this.categoryDrawable = categoryDrawable;
    }

//    public Contact getContact() {
//        return contact;
//    }
//
//    public void setContact(Contact contact) {
//        this.contact = contact;
//    }

//    private Event(Parcel in) {
//        id = in.readInt();
//        title = in.readString();
//        placeHolderFilepath = in.readString();
//        description = in.readString();
//        startDateTime = in.readLong();
//        endDateTime = in.readLong();
//        location = (Location) in.readParcelable(Location.class.getClassLoader());
//        categoryDrawable = in.readInt();
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
//        dest.writeString(title);
//        dest.writeString(placeHolderFilepath);
//        dest.writeString(description);
//        dest.writeLong(startDateTime);
//        dest.writeLong(endDateTime);
//        dest.writeParcelable(location, flags);
//        dest.writeInt(categoryDrawable);
//    }
//
//    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
//
//        @Override
//        public Event createFromParcel(Parcel source) {
//            return new Event(source);
//        }
//
//        @Override
//        public Event[] newArray(int size) {
//            return new Event[size];
//        }
//    };
}
