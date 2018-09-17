package com.kokutouda.dnote.dnote.ui;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kokutouda.dnote.dnote.R;

public class NotesActivity extends AppCompatActivity {

    public static final int ADD_NOTES_REQUEST = 999;
    public static final String KEY = "KEY_TYPE";
    public static final String TYPE_ADD = "ADD";
    public static final String TYPE_UPDATE = "UPDATE";

    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";


    private EditText mTitleEdit;
    private EditText mContentEdit;

    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
//        Intent intent = getIntent();
//        mType = intent.getStringExtra(KEY);

        initView();
    }

    private void initView() {
        mTitleEdit = findViewById(R.id.editTitle);
        mContentEdit = findViewById(R.id.editContent);
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
        if (content.equals("") && title.equals("")) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra(KEY_TITLE, title);
            intent.putExtra(KEY_CONTENT, content);
            setResult(RESULT_OK, intent);
        }
    }
}
