package kidouchi.chronobook.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iuy407 on 11/19/15.
 */
public class Category extends RealmObject {

    @PrimaryKey
    private int id;

    private String title;
    private String imgFilepath;

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

    public String getImgFilepath() {
        return imgFilepath;
    }

    public void setImgFilepath(String imgFilepath) {
        this.imgFilepath = imgFilepath;
    }
}
