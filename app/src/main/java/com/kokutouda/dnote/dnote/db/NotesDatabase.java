package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Notes.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    public static String DB_NAME = "notes";

    public abstract NotesDao noteDao();

}
