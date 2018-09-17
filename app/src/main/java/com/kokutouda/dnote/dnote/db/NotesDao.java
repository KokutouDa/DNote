package com.kokutouda.dnote.dnote.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void insertNotes(Notes... notes);

    //todo 会发生什么？
    @Insert
    void insertNotesList(Notes notes, List<Notes> subNotes);

    @Delete
    void deleteNotes(Notes notes);


    @Query("SELECT * FROM Notes")
    LiveData<List<Notes>> getAllNotes();
}
