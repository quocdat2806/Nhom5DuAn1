package com.example.foodapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodapp.dao.UserDAO;
import com.example.foodapp.modal.User;

@Database(entities = {User.class},version = 1)
public  abstract  class UserDatabase extends RoomDatabase {
    public static final String DB_NAME="USER.DB";
    public static  UserDatabase instance;
    public static  synchronized UserDatabase getInstance(Context context){
        if(instance==null){
            instance=Room.databaseBuilder(context.getApplicationContext(),UserDatabase.class,DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }
    public  abstract UserDAO userDAO();
}
