package com.kokutouda.dnote.dnote.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.kokutouda.dnote.dnote.DialogErrorBinding;
import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Category;
import com.kokutouda.dnote.dnote.db.Notes;
import com.kokutouda.dnote.dnote.util.DialogUtil;
import com.kokutouda.dnote.dnote.viewmodel.CategoryListViewModel;

import java.util.List;

public class NotesActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    public static final int ADD_NOTES_REQUEST = 999;

    public static final String KEY_NOTES = "notes";

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private AlertDialog mDialogNewCategory;

    private Notes mExistedNotes;
    private CategoryListViewModel mCategoryModel;
    private CategoryDialogAdapter mAdapter;
    private Integer mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initView();

    }

    private void initView() {
        mCategoryModel = ViewModelProviders.of(this).get(CategoryListViewModel.class);
        mAdapter = new CategoryDialogAdapter(this, R.layout.item_category_main);
        mCategoryModel.getCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                mAdapter.setCategoryList(categories);
            }
        });

        mEditTextTitle = findViewById(R.id.editTitle);
        mEditTextContent = findViewById(R.id.editContent);

        Intent intent = getIntent();
        mExistedNotes = (Notes) intent.getSerializableExtra(KEY_NOTES);
        if (mExistedNotes != null) {
            mEditTextTitle.setText(mExistedNotes.title);
            mEditTextContent.setText(mExistedNotes.content);
            mCategoryId = mExistedNotes.categoryId;
        }
    }

    @Override
    public void onBackPressed() {
        setNotesResult();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setNotesResult();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notes_category:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
                builder.setTitle(R.string.dialog_category_title)
                        .setAdapter(mAdapter, this)
                        .setPositiveButton(R.string.dialog_add_category, this)
                        .setNegativeButton(R.string.dialog_remove_category, this)
                        .show();
                return true;
            case R.id.menu_notes_tag:
                return true;
            case R.id.menu_notes_attachment:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void setNotesResult() {
        String title = mEditTextTitle.getText().toString();
        String content = mEditTextContent.getText().toString();
        if (isNotContent()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            if (mExistedNotes == null) {
                mExistedNotes = new Notes(title, content);
            } else {
                mExistedNotes.title = title;
                mExistedNotes.content = content;
            }
            mExistedNotes.categoryId = mCategoryId;
            intent.putExtra(KEY_NOTES, mExistedNotes);
            setResult(RESULT_OK, intent);
        }
    }

    private boolean isNotContent() {
        return (mEditTextTitle.getText().toString().equals("") &&
                mEditTextContent.getText().toString().equals(""));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            createDialogNewCategory();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            removeFromCategory();
        } else if (which >= 0) {
            addToCategory(which);

        }
    }

    private void createDialogNewCategory() {
        final View viewDialogEdit = getLayoutInflater().inflate(R.layout.dialog_category_edit, null);
        AlertDialog alertDialog = DialogUtil.createCategoryDialog(this, viewDialogEdit, null);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.all_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editTextDialog = viewDialogEdit.findViewById(R.id.edit_dialog);
                String categoryName = editTextDialog.getText().toString();
                if (!categoryName.equals("")) {
                    Category category = new Category(categoryName);
                    mCategoryModel.insertCategory(category);
                }
            }
        });
        alertDialog.show();
    }

    private boolean isDialogButtonClickable(AlertDialog dialog, int witchButton) {
        return dialog.getButton(witchButton).isClickable();
    }

    private void setDialogButtonClickable(AlertDialog dialog, int witchButton, boolean clickable) {
        dialog.getButton(witchButton).setClickable(clickable);
    }

    //笔记添加到分类中
    private void addToCategory(int which) {
        Category category = (Category) mAdapter.getItem(which);
        if (category != null) {
            if (mCategoryId != null) {
                if (!mCategoryId.equals(category.id)) {
                    mCategoryModel.changeCategory(mCategoryId, category.id);
                }
            } else {
                mCategoryModel.changeCategory(null, category.id);

            }
            mCategoryId = category.id;
        }
    }

    private void removeFromCategory() {
        if (mCategoryId != null) {
            mCategoryModel.changeCategory(mCategoryId, null);
            mCategoryId = null;
        }
    }
}
