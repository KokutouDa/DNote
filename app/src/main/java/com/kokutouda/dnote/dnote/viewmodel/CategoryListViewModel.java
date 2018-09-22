package com.kokutouda.dnote.dnote.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kokutouda.dnote.dnote.db.Category;
import com.kokutouda.dnote.dnote.db.NotesRepository;

import java.util.List;

public class CategoryListViewModel extends AndroidViewModel {

    private NotesRepository mRepository;
    private LiveData<List<Category>> mCategory;

    public CategoryListViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NotesRepository(application);
        mCategory = mRepository.getAllCategory();
    }

    public LiveData<List<Category>> getCategory() {
        return this.mCategory;
    }

    public void insertCategory(Category category) {
        mRepository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        mRepository.updateCategory(category);
    }
}
