package com.kokutouda.dnote.dnote.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

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
        return this.mNotes;
    }

    public void insertNotes(Notes notes) {
        mNotesRepository.insertNotes(notes);
    }

    public void updateNotes(Notes notes) {
        mNotesRepository.updateNotes(notes);
    }
}
