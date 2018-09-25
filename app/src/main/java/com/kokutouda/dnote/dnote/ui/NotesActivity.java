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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private AlertDialog mDialogNewCategory;
    private ImageView mImageError;
    private TextView mTextError;
    private Button mBtnPositiveError;

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
        mAdapter = new CategoryAdapter(this, R.layout.dialog_category_item);
        mViewModel.getCategory().observe(this, new Observer<List<Category>>() {
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
                intent.putExtra(KEY_POSITION, POSITION_NEW_NOTES);
                mExistedNotes = new Notes(title, content);
            }
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

        } else if (which >= 0) {

        }
    }

    private void createDialogNewCategory() {
        final View viewDialogEdit = getLayoutInflater().inflate(R.layout.dialog_category_edit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogEditTheme);

        mDialogNewCategory = builder.setTitle(R.string.dialog_edit_category)
                .setView(viewDialogEdit)
                .setPositiveButton(R.string.all_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextDialog = viewDialogEdit.findViewById(R.id.edit_dialog);
                        String categoryName = editTextDialog.getText().toString();
                        if (!categoryName.equals("")) {
                            Category category = new Category(categoryName);
                            mViewModel.insertCategory(category);
                        }
                    }
                }).create();

        mDialogNewCategory.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                setDialogButtonClickable(mDialogNewCategory, DialogInterface.BUTTON_POSITIVE, false);
                mDialogNewCategory.getButton(DialogInterface.BUTTON_POSITIVE).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!isDialogButtonClickable(mDialogNewCategory, DialogInterface.BUTTON_POSITIVE)) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                setDialogErrorVisibility(mDialogNewCategory, View.VISIBLE);
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        EditText editTextCategoryName = viewDialogEdit.findViewById(R.id.edit_dialog);
        editTextCategoryName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean clickable = !s.toString().equals("");
                setDialogButtonClickable(mDialogNewCategory, DialogInterface.BUTTON_POSITIVE, clickable);
                if (clickable) {
                    setDialogErrorVisibility(mDialogNewCategory, View.GONE);
                }
            }
        });
        mDialogNewCategory.show();
    }

    private boolean isDialogButtonClickable(AlertDialog dialog, int witchButton) {
        return dialog.getButton(witchButton).isClickable();
    }

    private void setDialogButtonClickable(AlertDialog dialog, int witchButton, boolean clickable) {
        dialog.getButton(witchButton).setClickable(clickable);
    }

    private void setDialogErrorVisibility(AlertDialog dialog, int visibility) {
        if (mTextError == null) {
            mTextError = dialog.findViewById(R.id.text_error);
        }
        if (mImageError == null) {
            mImageError = dialog.findViewById(R.id.image_error);
        }
        mTextError.setVisibility(visibility);
        mImageError.setVisibility(visibility);
    }
}
