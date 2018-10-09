package com.kokutouda.dnote.dnote.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void insertNotesList(Notes... notes);

    @Insert
    long insertNotesSingle(Notes notes);

    @Delete
    void deleteNotes(Notes... notes);

    @Query("SELECT * FROM Notes WHERE delete_time IS NULL")
    LiveData<List<Notes>> getAllNotes();

    @Query("SELECT * FROM Notes WHERE category_id == :categoryId")
    LiveData<List<Notes>> getNotesByCategory(Integer categoryId);

    @Query("SELECT * FROM Notes WHERE delete_time IS NOT NULL")
    LiveData<List<Notes>> getDeletedNotes();

    @Update
    void updateNotes(Notes... notes);


    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAllCategory();

    @Query("SELECT * FROM Category WHERE id = :id")
    Category getCategoryById(Integer id);

    @Update
    void updateCategory(Category... category);

    @Insert
    void insertCategory(Category... category);


    @Insert
    void insertAttachment(Attachment... attachments);

    @Query("SELECT * FROM Attachment WHERE notes_id IS :notesId")
    LiveData<List<Attachment>> getAttachmentByNotesId(Integer notesId);

    @Delete
    void deleteAttachment(Attachment... attachments);

    @Update
    void updateAttachment(Attachment... attachments);

    @Query("SELECT * FROM Attachment WHERE notes_id IS NULL")
    List<Attachment> getAttachmentNoNotesId();

}
