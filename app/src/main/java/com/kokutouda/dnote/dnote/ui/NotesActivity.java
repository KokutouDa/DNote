package com.kokutouda.dnote.dnote.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Attachment;
import com.kokutouda.dnote.dnote.db.Category;
import com.kokutouda.dnote.dnote.db.Notes;
import com.kokutouda.dnote.dnote.db.NotesRepository;
import com.kokutouda.dnote.dnote.util.BitmapUtils;
import com.kokutouda.dnote.dnote.util.DialogUtils;
import com.kokutouda.dnote.dnote.viewmodel.AttachmentViewModel;
import com.kokutouda.dnote.dnote.viewmodel.CategoryListViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private static final String TAG = "NotesActivity";

    private static final String AUTHORITY_IMAGE = "com.kokutouda.dnote.fileprovider";

    public static final int REQUEST_ADD_NOTES = 999;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final String KEY_NOTES = "com.kokutouda.dnote.dnote.Notes";

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private Notes mExistedNotes;

    private GridView mGridView;
    private AttachmentAdapter mAttachmentAdapter;
    private AttachmentViewModel mAttachmentModel;
    private String mCurrentImagePath;

    private CategoryListViewModel mCategoryModel;
    private CategoryDialogAdapter mCategoryAdapter;
    private Integer mCategoryId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initView();
    }

    private void initView() {
        final LifecycleOwner owner = this;
        mCategoryModel = ViewModelProviders.of(this).get(CategoryListViewModel.class);
        mCategoryAdapter = new CategoryDialogAdapter(this, R.layout.item_category_main);
        mCategoryModel.getCategory().observe(owner, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                mCategoryAdapter.setCategoryList(categories);
            }
        });

        mEditTextTitle = findViewById(R.id.edit_title);
        mEditTextContent = findViewById(R.id.edit_content);
        mGridView = findViewById(R.id.grid_view);

        mAttachmentAdapter = new AttachmentAdapter(1);
        mGridView.setAdapter(mAttachmentAdapter);
        mAttachmentModel = new AttachmentViewModel(getApplication());
        NotesRepository.AttachmentsCallback attachmentCallback = new NotesRepository.AttachmentsCallback() {
            @Override
            public void callback(LiveData<List<Attachment>> attachments) {
                attachments.observe(owner, new Observer<List<Attachment>>() {
                    @Override
                    public void onChanged(@Nullable List<Attachment> attachments) {
                        mAttachmentAdapter.setAttachments(attachments);
                        if (attachments.size() > 1) {
                            mGridView.setNumColumns(2);
                            mAttachmentAdapter.setNumColumns(2);
                        }
                    }
                });
            }
        };

        Intent intent = getIntent();
        mExistedNotes = (Notes) intent.getSerializableExtra(KEY_NOTES);
        if (mExistedNotes != null) {
            mAttachmentModel.getAttachmentByNotes(mExistedNotes.id, attachmentCallback);
            mEditTextTitle.setText(mExistedNotes.title);
            mEditTextContent.setText(mExistedNotes.content);
            mCategoryId = mExistedNotes.categoryId;
        } else {
            mAttachmentModel.getAttachmentByNotes(null, attachmentCallback);
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
                        .setAdapter(mCategoryAdapter, this)
                        .setPositiveButton(R.string.dialog_add_category, this)
                        .setNegativeButton(R.string.dialog_remove_category, this)
                        .show();
                return true;
            case R.id.menu_notes_tag:
                return true;
            case R.id.menu_notes_attachment:
                dispatchTakePictureIntent();
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
                mEditTextContent.getText().toString().equals("") && mAttachmentAdapter.getCount() == 0);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = BitmapUtils.createImageFiles(this);
                mCurrentImagePath = imageFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                Uri fileUri = FileProvider.getUriForFile(this,
                        AUTHORITY_IMAGE, imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void createDialogNewCategory() {
        final View viewDialogEdit = getLayoutInflater().inflate(R.layout.dialog_category_edit, null);
        AlertDialog alertDialog = DialogUtils.createCategoryDialog(this, viewDialogEdit, null);
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

    //笔记添加到分类中
    private void addToCategory(int which) {
        Category category = (Category) mCategoryAdapter.getItem(which);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, mCurrentImagePath);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mExistedNotes != null) {
                mAttachmentModel.insertAttachment(new Attachment(mCurrentImagePath, mExistedNotes.id));
            } else {
                mAttachmentModel.insertAttachment(new Attachment(mCurrentImagePath));
            }
        }
    }
}
