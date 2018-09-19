package com.kokutouda.dnote.dnote.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Notes;

public class NotesActivity extends AppCompatActivity {

    public static final int ADD_NOTES_REQUEST = 999;

    public static final int POSITION_NEW_NOTES = -1;
    public static final int POSITION_EXISTED_NOTES = 99;
    public static final String KEY_POSITION = "position";
    public static final String KEY_NOTES = "notes";

    private EditText mTitleEdit;
    private EditText mContentEdit;

    private Notes mExistedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initView();
    }

    private void initView() {
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

    private void setNotesResult() {
        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        if (isNotContent()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            if (mExistedNotes == null) {
                intent.putExtra(KEY_POSITION, POSITION_NEW_NOTES);
                mExistedNotes = new Notes();
            }
            mExistedNotes.title = title;
            mExistedNotes.content = content;
            intent.putExtra(KEY_NOTES, mExistedNotes);
            setResult(RESULT_OK, intent);
        }
    }

    public boolean isNotContent() {
        return (mTitleEdit.getText().toString().equals("") &&
                mContentEdit.getText().toString().equals(""));
    }
}
