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

    @Query("SELECT * FROM Notes WHERE delete_time == 0")
    LiveData<List<Notes>> getAllNotes();

    @Query("SELECT * FROM Notes WHERE delete_time != 0")
    LiveData<List<Notes>> getDeletedNotes();

    @Update
    void updateNotes(Notes... notes);

    @Query("SELECT * FROM Notes WHERE id LIKE :categoryId")
    LiveData<List<Notes>> getNotesByCategory(int categoryId);

    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAllCategory();

    @Query("SELECT * FROM Category WHERE id = :id")
    Category getCategoryById(Integer id);

    @Update
    void updateCategory(Category... category);

    @Insert
    void insertCategory(Category... category);
}
