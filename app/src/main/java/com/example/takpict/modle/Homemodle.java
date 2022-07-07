package com.example.takpict.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class Homemodle implements Parcelable {

    private int id;
    private String text;
    private int fromUser;
    private String image;

    public Homemodle() {
    }

    public Homemodle(String text, String image, int fromUser) {
        this.text = text;
        this.image = image;
        this.fromUser = fromUser;
    }

    protected Homemodle(Parcel in) {
        id = in.readInt();
        text = in.readString();
        image = in.readString();
        fromUser = in.readInt();
    }

    public static final Creator<Homemodle> CREATOR = new Creator<Homemodle>() {
        @Override
        public Homemodle createFromParcel(Parcel in) {
            return new Homemodle(in);
        }

        @Override
        public Homemodle[] newArray(int size) {
            return new Homemodle[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public int getFromUser() {
        return fromUser;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public static final String TABLE_NAME = "home";
    public static final String COL_ID = "id";
    public static final String COL_Text = "text";
    public static final String COL_Image = "image";
    public static final String COL_USER = "user";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_Text + " TEXT NOT NULL," +
            COL_Image + " TEXT NOT NULL," +
            COL_USER + " INTEGER NOT NULL)";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeInt(fromUser);
        dest.writeString(image);
    }
}
