package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Attachment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    @ColumnInfo(name = "file_name")
    public String fileName;

    @ColumnInfo(name = "notes_id")
    public Integer notesId;

    public Attachment(@NonNull String fileName) {
        this.fileName = fileName;
    }

    @Ignore
    public Attachment(@NonNull String fileName, Integer notesId) {
        this.fileName = fileName;
        this.notesId = notesId;
    }
}
