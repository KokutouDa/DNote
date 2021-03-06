package com.kokutouda.dnote.dnote.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class NotesRepository {

    public static final int ACTION_COUNT_INCREASE = 1;
    public static final int ACTION_COUNT_DECREASE = -1;

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

    public void getAll(final NotesListCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.callback(mNotesDao.getAllNotes());
            }
        }).start();
    }

    public void getByCategory(int categoryId, NotesListCallback callback) {
        new Thread(new GetNotesByCategoryRunnable(mNotesDao, categoryId, callback)).start();
    }

    public void insertNotesList(Notes... notes) {
        new InsertNotesAsyncTask(mNotesDao).execute(notes);
    }

    public void insertNotesSingle(final Notes notes, final IdCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long id = mNotesDao.insertNotesSingle(notes);
                if (callback != null) {
                    callback.callback(id);
                }
            }
        }).start();
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
        new Thread(new UpdateCategoryRunnable(mNotesDao, category)).start();
    }

    public void changeCategoryCount(Integer categoryId, int action) {
        new Thread(new ChangeCountRunnable(mNotesDao, categoryId, action)).start();
    }


    public void insertAttachment(final Attachment... attachments) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mNotesDao.insertAttachment(attachments);
            }
        }).start();
    }

    public void deleteAttachment(final Attachment... attachments) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mNotesDao.deleteAttachment(attachments);
            }
        }).start();
    }

    public void updateAttachment(final Attachment... attachments) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mNotesDao.updateAttachment(attachments);
            }
        }).start();
    }

    public void getAttachmentByNotes(final Integer notesId, final AttachmentsCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LiveData<List<Attachment>> data = mNotesDao.getAttachmentByNotesId(notesId);
                callback.callback(data);
            }
        }).start();
    }

    public void getAttachmentNoNotesId(final AttachmentListCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.callback(mNotesDao.getAttachmentNoNotesId());
            }
        }).start();
    }



    public interface IdCallback {
        void callback(long notesId);
    }

    public interface NotesListCallback {
        void callback(LiveData<List<Notes>> notesList);
    }

    public interface AttachmentsCallback {
        void callback(LiveData<List<Attachment>> attachments);
    }

    public interface AttachmentListCallback {
        void callback(List<Attachment> attachmentList);
    }



    private static class InsertNotesAsyncTask extends AsyncTask<Notes, Void, Void> {
        private NotesDao notesDao;

        InsertNotesAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Notes... notes) {
            notesDao.insertNotesList((Notes) notes[0]);
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

    private class ChangeCountRunnable implements Runnable {
        private Integer categoryId;
        private NotesDao notesDao;
        private int action;

        ChangeCountRunnable(NotesDao notesDao, Integer categoryId, int action) {
            this.notesDao = notesDao;
            this.categoryId = categoryId;
            this.action = action;
        }

        @Override
        public void run() {
            Category category = notesDao.getCategoryById(this.categoryId);
            if (action == ACTION_COUNT_INCREASE) {
                category.count = category.count + 1;
            } else if (action == ACTION_COUNT_DECREASE) {
                category.count = category.count - 1;
            }
            notesDao.updateCategory(category);
        }
    }

    private class GetNotesByCategoryRunnable implements Runnable {
        private Integer categoryId;
        private NotesDao notesDao;
        NotesListCallback callback;

        GetNotesByCategoryRunnable(NotesDao notesDao, Integer categoryId, NotesListCallback callback) {
            this.categoryId = categoryId;
            this.notesDao = notesDao;
            this.callback = callback;
        }

        @Override
        public void run() {
            callback.callback(notesDao.getNotesByCategory(categoryId));

        }
    }

}
