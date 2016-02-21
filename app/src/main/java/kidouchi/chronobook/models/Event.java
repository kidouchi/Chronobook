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
    private int categoryDrawable;

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

    public int getCategoryDrawable() {
        return categoryDrawable;
    }

    public void setCategoryDrawable(int categoryDrawable) {
        this.categoryDrawable = categoryDrawable;
    }
}
