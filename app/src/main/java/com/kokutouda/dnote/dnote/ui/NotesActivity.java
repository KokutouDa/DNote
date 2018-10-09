package com.kokutouda.dnote.dnote.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    public static final int REQUEST_ADD_NOTES = 999;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final String KEY_NOTES = "notes";
    public static final String KEY_ATTACHMENT = "attachment";

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private Notes mExistedNotes;

    private GridView mGridView;
    private AttachmentAdapter mAttachmentAdapter;
    private AttachmentViewModel mAttachmentModel;

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

        mAttachmentAdapter = new AttachmentAdapter();
        mGridView.setAdapter(mAttachmentAdapter);
        mAttachmentModel = new AttachmentViewModel(getApplication());
        final NotesRepository.AttachmentsCallback attachmentCallback = new NotesRepository.AttachmentsCallback() {
            @Override
            public void callback(LiveData<List<Attachment>> attachments) {
                attachments.observe(owner, new Observer<List<Attachment>>() {
                    @Override
                    public void onChanged(@Nullable List<Attachment> attachments) {
                        mAttachmentAdapter.setAttachments(attachments);
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
        //todo 新的笔记只有在插入的时候才有notesId的。
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGridView.invalidate();
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
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                        && takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
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

//            if (mAttachmentAdapter.getCount() != 0) {
//                intent.putExtra(KEY_ATTACHMENT, mAttachmentAdapter.getAttachments());
//            }
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap image = (Bitmap) bundle.get("data");
            String fileName = BitmapUtils.saveImage(this, image);
            if (mExistedNotes != null) {
                mAttachmentModel.insertAttachment(new Attachment(fileName, mExistedNotes.id));
            } else {
                mAttachmentModel.insertAttachment(new Attachment(fileName));
            }
        }
    }
}
