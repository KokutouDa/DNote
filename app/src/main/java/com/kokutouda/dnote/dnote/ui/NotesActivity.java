package com.kokutouda.dnote.dnote.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Category;
import com.kokutouda.dnote.dnote.db.Notes;
import com.kokutouda.dnote.dnote.viewmodel.CategoryListViewModel;

import java.util.List;

public class NotesActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    public static final int ADD_NOTES_REQUEST = 999;

    public static final int POSITION_NEW_NOTES = -1;
    public static final int POSITION_EXISTED_NOTES = 99;
    public static final String KEY_POSITION = "position";
    public static final String KEY_NOTES = "notes";

    private EditText mTitleEdit;
    private EditText mContentEdit;

    private Notes mExistedNotes;
    private CategoryListViewModel mViewModel;
    private CategoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initView();
    }

    private void initView() {
        mViewModel = ViewModelProviders.of(this).get(CategoryListViewModel.class);
        mAdapter = new CategoryAdapter(this, R.layout.simple_list_item);
        mViewModel.getCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                mAdapter.setCategoryList(categories);
            }
        });

        mTitleEdit = findViewById(R.id.editTitle);
        mContentEdit = findViewById(R.id.editContent);

        Intent intent = getIntent();
        mExistedNotes = (Notes) intent.getSerializableExtra(KEY_NOTES);
        if (mExistedNotes != null) {
            mTitleEdit.setText(mExistedNotes.title);
            mContentEdit.setText(mExistedNotes.content);
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
        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        if (isNotContent()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            if (mExistedNotes == null) {
                intent.putExtra(KEY_POSITION, POSITION_NEW_NOTES);
                mExistedNotes = new Notes(title, content);
            }
            intent.putExtra(KEY_NOTES, mExistedNotes);
            setResult(RESULT_OK, intent);
        }
    }

    private boolean isNotContent() {
        return (mTitleEdit.getText().toString().equals("") &&
                mContentEdit.getText().toString().equals(""));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {

        } else if (which == DialogInterface.BUTTON_NEGATIVE) {

        } else if (which >= 0) {

        }
    }
}
