package com.kokutouda.dnote.dnote.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Checkable;
import android.widget.EditText;

import com.kokutouda.dnote.dnote.DialogErrorBinding;
import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Category;
import com.kokutouda.dnote.dnote.db.Notes;
import com.kokutouda.dnote.dnote.util.DialogUtil;
import com.kokutouda.dnote.dnote.util.NavigationViewUtils;
import com.kokutouda.dnote.dnote.viewmodel.CategoryListViewModel;
import com.kokutouda.dnote.dnote.viewmodel.NotesListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int POSITION_NEW_NOTES = -1;

    private RecyclerView mRecyclerView;
    private Context mContext;
    private NotesAdapter mNotesAdapter;
    private NotesListViewModel mNotesModel;
    private CategoryListViewModel mCategoryModel;
    private RecyclerView mCategoryView;
    private CategoryNavAdapter mCategoryNavAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    public void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNotesAtyForResult(POSITION_NEW_NOTES);
            }
        });

        //drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);

        mCategoryNavAdapter = new CategoryNavAdapter(NavigationViewUtils.getHeaderData(mContext)
                , NavigationViewUtils.getFooterData(mContext));
        mCategoryNavAdapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mCategoryView.getChildAdapterPosition(v);
                mCategoryNavAdapter.notifyItemChanged(position, "any");

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mCategoryNavAdapter.setItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = mCategoryView.getChildAdapterPosition(v);
                createDialogEditCategory(position);
                return true;
            }
        });

        mCategoryView = findViewById(R.id.recyclerview_nav_categories);
        mCategoryView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryView.setAdapter(mCategoryNavAdapter);

        mCategoryModel = ViewModelProviders.of(this).get(CategoryListViewModel.class);
        mCategoryModel.getCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                mCategoryNavAdapter.setCategoryList(categories);
            }
        });

        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.addOnItemTouchListener(new SimpleOnItemTouchListener(this, mRecyclerView,
                new SimpleOnItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        startNotesAtyForResult(position);
                    }
                },
                new SimpleOnItemTouchListener.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View v, int position) {

                    }
                }));
        mNotesModel = ViewModelProviders.of(this).get(NotesListViewModel.class);
        mNotesModel.getAll().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {
                mNotesAdapter.setNotesList(notes);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mNotesAdapter = new NotesAdapter();
        mRecyclerView.setAdapter(mNotesAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == NotesActivity.ADD_NOTES_REQUEST) {
            if (resultCode == RESULT_OK) {
                Notes notes = (Notes) data.getSerializableExtra("notes");

                if (notes.id == null) {
                    mNotesModel.insertNotes(notes);
                } else {
                    mNotesModel.updateNotes(notes);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param position -1时表示新数据
     */
    private void startNotesAtyForResult(int position) {
        Intent intent = new Intent(mContext, NotesActivity.class);
        if (position != POSITION_NEW_NOTES) {

            Notes note = mNotesAdapter.getItem(position);
            intent.putExtra(NotesActivity.KEY_NOTES, note);
        }
        startActivityForResult(intent, NotesActivity.ADD_NOTES_REQUEST);
    }

    private void createDialogEditCategory(final int position) {
        final View viewDialogEdit = getLayoutInflater().inflate(R.layout.dialog_category_edit, null);
        final Category category = mCategoryNavAdapter.getItemByLayoutPosition(position);
        final EditText editText = viewDialogEdit.findViewById(R.id.edit_dialog);

        AlertDialog alertDialog = DialogUtil.createCategoryDialog(this, viewDialogEdit, category.name);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.all_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                category.name = editText.getText().toString();
                mCategoryModel.updateCategory(category);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.all_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}
