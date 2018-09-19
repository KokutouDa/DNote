package com.kokutouda.dnote.dnote.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class NotesRepository {

    private LiveData<List<Notes>> mAllNotes;
    private LiveData<List<Category>> mAllCategory;
    private NotesDao mNotesDao;

    public NotesRepository(Context applicationContext) {
        mNotesDao = NotesDatabase.getDatabase(applicationContext).noteDao();
        mAllNotes = mNotesDao.getAllNotes();
        mAllCategory = mNotesDao.getAllCategory();
    }

    public LiveData<List<Notes>> getAll() {
        return mAllNotes;
    }

    public void insertNotes(Notes notes) {
        new InsertNotesAsyncTask(mNotesDao).execute(notes);
    }

    public void updateNotes(Notes notes) {
        new Thread(new UpdateNotesRunnable(mNotesDao, notes)).start();
    }

    public LiveData<List<Category>> getAllCategory() {
        return this.mAllCategory;
    }

    public void insertCategory(Category category) {
        new Thread(new InsertCategoryRunnable(this.mNotesDao, category)).start();
    }

    public void updateCategory(Category category) {
        new Thread(new UpdateCategoryRunnable(mNotesDao, category));
    }

    private static class InsertNotesAsyncTask extends AsyncTask<Notes, Void, Void> {
        private NotesDao notesDao;

        InsertNotesAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Notes... notes) {
            notesDao.insertNotes((Notes) notes[0]);
            return null;
        }
    }

    private class UpdateNotesRunnable implements Runnable {
        private Notes notes;
        private NotesDao notesDao;

        UpdateNotesRunnable(NotesDao notesDao, Notes notes) {
            this.notes = notes;
            this.notesDao = notesDao;
        }
        @Override
        public void run() {
            notesDao.updateNotes(notes);
        }
    }

    private class InsertCategoryRunnable implements Runnable {
        private Category category;
        private NotesDao notesDao;

        InsertCategoryRunnable (NotesDao notesDao, Category category) {
            this.category = category;
            this.notesDao = notesDao;
        }
        @Override
        public void run() {
            notesDao.insertCategory(this.category);
        }
    }

    private class UpdateCategoryRunnable implements Runnable {
        private Category category;
        private NotesDao notesDao;

        UpdateCategoryRunnable (NotesDao notesDao, Category category) {
            this.category = category;
            this.notesDao = notesDao;
        }
        @Override
        public void run() {
            notesDao.updateCategory(this.category);
        }
    }



}
