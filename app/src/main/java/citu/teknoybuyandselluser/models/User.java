package citu.teknoybuyandselluser.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 ** Created by jack on 2/02/16.
 */
public class User extends RealmObject {
    @PrimaryKey
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
