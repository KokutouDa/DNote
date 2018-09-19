package com.kokutouda.dnote.dnote.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class NotesRepository {

    private LiveData<List<Notes>> mAllNotes;
    private NotesDao mNotesDao;

    public NotesRepository(Context applicationContext) {
        mNotesDao = NotesDatabase.getDatabase(applicationContext).noteDao();
        mAllNotes = mNotesDao.getAllNotes();
    }

    public LiveData<List<Notes>> getAll() {
        return mAllNotes;
    }

    public void insertNotes(Notes notes) {
        new InsertAsyncTask(mNotesDao).execute(notes);
    }

    public void updateNotes(Notes notes) {
        new Thread(new UpdateRunnable(mNotesDao, notes)).start();
    }

    private static class InsertAsyncTask extends AsyncTask<Notes, Void, Void> {
        private NotesDao notesDao;

        InsertAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Notes... notes) {
            notesDao.insertNotes((Notes) notes[0]);
            return null;
        }
    }

    private class UpdateRunnable implements Runnable {
        private Notes notes;
        private NotesDao notesDao;

        UpdateRunnable(NotesDao notesDao, Notes notes) {
            this.notes = notes;
            this.notesDao = notesDao;
        }
        @Override
        public void run() {
            notesDao.updateNotes(notes);
        }
    }



}
