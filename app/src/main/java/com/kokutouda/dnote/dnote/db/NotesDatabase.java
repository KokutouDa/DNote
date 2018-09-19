package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {Notes.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    public static String DB_NAME = "notes";

    private static NotesDatabase INSTANCE;

    public abstract NotesDao noteDao();

    public static NotesDatabase getDatabase(Context applicationContext) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(applicationContext, NotesDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
