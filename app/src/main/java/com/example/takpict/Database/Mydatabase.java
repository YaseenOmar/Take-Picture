package com.example.takpict.Database;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.takpict.modle.Homemodle;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class Mydatabase extends SQLiteOpenHelper {
    SQLiteDatabase database;

    public Mydatabase(@Nullable Context context) {
        super(context, "Takpict", null, 2);
        database = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Homemodle.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Homemodle.TABLE_NAME);
        onCreate(db);

    }

    public boolean insertitem(String text, String image, int isFromUser) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Homemodle.COL_Text, text);
        contentValues.put(Homemodle.COL_Image, image);
        contentValues.put(Homemodle.COL_USER, isFromUser);
        return database.insert(Homemodle.TABLE_NAME, null, contentValues) > 0;
    }


    public ArrayList<Homemodle> getItems() {
        ArrayList<Homemodle> homeitems = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + Homemodle.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            Homemodle homeitem = new Homemodle();
            homeitem.setId(cursor.getInt(cursor.getColumnIndex(Homemodle.COL_ID)));
            homeitem.setText(cursor.getString(cursor.getColumnIndex(Homemodle.COL_Text)));
            homeitem.setImage(cursor.getString(cursor.getColumnIndex(Homemodle.COL_Image)));
            homeitem.setFromUser(cursor.getInt(cursor.getColumnIndex(Homemodle.COL_USER)));
            homeitems.add(homeitem);
        }
        cursor.close();
        return homeitems;
    }

   public boolean deletepost(int id) {
   return database.delete(Homemodle.TABLE_NAME, Homemodle.COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

}
