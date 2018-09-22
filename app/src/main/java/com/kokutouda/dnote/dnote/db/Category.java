package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;


    public String name;

    public int count;

    public Category(String name) {
        this.name = name;
        this.count = 0;
    }

    @Ignore
    public Category(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
