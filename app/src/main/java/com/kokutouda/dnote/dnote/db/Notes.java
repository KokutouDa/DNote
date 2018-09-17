package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity
public class Notes {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String content;

}
