package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.PrimaryKey;

public class CategoryNotes {

    @PrimaryKey
    public int categoryId;

    @PrimaryKey
    public int notesId;

    public CategoryNotes(int categoryId, int notesId) {
        this.categoryId = categoryId;
        this.notesId = notesId;
    }
}
