package com.kokutouda.dnote.dnote.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.kokutouda.dnote.dnote.db.Notes;

public class EditNotesViewModel extends ViewModel {
    private MutableLiveData<Notes> mExistedNotes;

    public MutableLiveData<Notes> getExistedNotes() {
        if (this.mExistedNotes == null) {
            this.mExistedNotes = new MutableLiveData<Notes>();
        }
        return this.mExistedNotes;
    }

    public void setValue(Notes notes) {
        mExistedNotes.setValue(notes);
    }
}
