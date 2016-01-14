package citu.teknoybuyandselluser.models;

/**
 * Created by jack on 14/01/16.
 */
public class Users {
    private int id;
    private String username;
    private boolean is_staff;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean is_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
