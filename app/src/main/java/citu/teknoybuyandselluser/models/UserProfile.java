package citu.teknoybuyandselluser.models;

import io.realm.RealmObject;

/**
 * Created by Batistil on 12/28/2015.
 */
public class UserProfile extends RealmObject{
    private User user;
    private Student student;
    private int stars_collected;
    private String picture;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getStars_collected() {
        return stars_collected;
    }

    public void setStars_collected(int stars_collected) {
        this.stars_collected = stars_collected;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
