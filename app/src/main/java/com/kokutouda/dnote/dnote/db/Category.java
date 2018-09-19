package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    public int id;


    public String name;

    @ColumnInfo(name = "notes_id")
    public int notesId;
}
