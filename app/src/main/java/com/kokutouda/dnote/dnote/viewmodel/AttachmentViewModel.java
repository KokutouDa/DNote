package com.kokutouda.dnote.dnote.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.kokutouda.dnote.dnote.db.Attachment;
import com.kokutouda.dnote.dnote.db.NotesRepository;

public class AttachmentViewModel extends AndroidViewModel {

    private NotesRepository mRepository;

    public AttachmentViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NotesRepository(application);
    }

    public void insertAttachment(Attachment... attachments) {
        mRepository.insertAttachment(attachments);
    }

    public void updateAttachment(Attachment... attachments) {
        mRepository.updateAttachment(attachments);
    }

    public void deleteAttachment(Attachment... attachments) {
        mRepository.deleteAttachment(attachments);
    }

    public void getAttachmentByNotes(Integer notesId, NotesRepository.AttachmentsCallback callback) {
        mRepository.getAttachmentByNotes(notesId, callback);
    }
}
