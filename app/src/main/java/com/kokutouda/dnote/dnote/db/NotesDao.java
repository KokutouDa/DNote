package com.kokutouda.dnote.dnote.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void insertNotes(Notes... notes);

    @Delete
    void deleteNotes(Notes... notes);


    @Query("SELECT * FROM Notes")
    LiveData<List<Notes>> getAllNotes();

    @Update
    void updateNotes(Notes... notes);
}
