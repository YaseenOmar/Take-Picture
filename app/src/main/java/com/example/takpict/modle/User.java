package com.example.takpict.modle;

import android.net.Uri;
import android.widget.EditText;

public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private String photo;


    public User() {

    }

    public User(EditText name, EditText email, EditText password) {

    }

    public User(String id ,String email, String password, String name,String photo) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.photo=photo;
        this.id=id;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
