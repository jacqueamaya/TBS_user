package citu.teknoybuyandselluser.models_old;

import io.realm.RealmObject;

/**
 ** Created by jack on 6/02/16.
 */
public class Category {
    private int id;
    private String category_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
