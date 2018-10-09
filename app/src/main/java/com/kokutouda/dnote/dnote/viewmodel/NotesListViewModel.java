package com.kokutouda.dnote.dnote.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.kokutouda.dnote.dnote.db.Attachment;
import com.kokutouda.dnote.dnote.db.Notes;
import com.kokutouda.dnote.dnote.db.NotesRepository;

import java.util.List;

public class NotesListViewModel extends AndroidViewModel {

    private NotesRepository mNotesRepository;
    private LiveData<List<Notes>> mNotes;

    public NotesListViewModel(@NonNull Application application) {
        super(application);
        mNotesRepository = new NotesRepository(application);
        mNotes = mNotesRepository.getAll();

    }

    public LiveData<List<Notes>> getAll() {
        return mNotes;
    }

    public void getAll(NotesRepository.NotesListCallback callback) {
        mNotesRepository.getAll(callback);
    }

    public void getByCategory(int categoryId, NotesRepository.NotesListCallback callback) {
        mNotesRepository.getByCategory(categoryId,callback);
    }

    public void insertNotesList(Notes... notes) {
        mNotesRepository.insertNotesList(notes);
    }

    public void insertNotesSingle(Notes notes) {
        mNotesRepository.insertNotesSingle(notes, new NotesRepository.IdCallback() {
            @Override
            public void callback(final long notesId) {
                getAttachmentNoNotesId(new NotesRepository.AttachmentListCallback() {
                    @Override
                    public void callback(List<Attachment> attachmentList) {
                        updateAttachmentListNotesId(notesId, attachmentList);
                    }
                });
            }
        });
    }

    public void updateNotes(Notes notes) {
        mNotesRepository.updateNotes(notes);
    }

    public void getAttachmentNoNotesId(NotesRepository.AttachmentListCallback callback) {
        mNotesRepository.getAttachmentNoNotesId(callback);
    }

    public void updateAttachment(Attachment... attachments) {
        mNotesRepository.updateAttachment(attachments);
    }

    public void updateAttachmentListNotesId(long notesId, List<Attachment> attachmentList) {
        if (attachmentList == null || attachmentList.size() == 0) {
            return;
        }
        Integer id = Integer.valueOf(String.valueOf(notesId));
        Attachment[] a = new Attachment[attachmentList.size()];
        for (int i = 0; i < attachmentList.size(); i++) {
            Attachment attachment = attachmentList.get(i);
            attachment.notesId = id;
            a[i] = attachment;
        }
        updateAttachment(a);
    }
}
