package com.kokutouda.dnote.dnote.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Notes;
import com.kokutouda.dnote.dnote.viewmodel.NotesListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private NotesAdapter mAdapter;
    private NotesListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initData();
    }

    public void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNotesAtyForResult(NotesActivity.POSITION_NEW_NOTES);
            }
        });

        //drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.addOnItemTouchListener(new NotesListOnItemTouchListener(this, new NotesListOnItemTouchListener.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                startNotesAtyForResult(position);
            }
        }));
    }

    public void initData() {
        mViewModel = ViewModelProviders.of(this).get(NotesListViewModel.class);
        mViewModel.getAll().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {
                mAdapter.setNotesList(notes);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new NotesAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == NotesActivity.ADD_NOTES_REQUEST) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(NotesActivity.KEY_POSITION, NotesActivity.POSITION_EXISTED_NOTES);
                Notes notes = (Notes) data.getSerializableExtra("notes");

                if (position == NotesActivity.POSITION_NEW_NOTES) {
                    mViewModel.insertNotes(notes);
                } else {
                    mViewModel.updateNotes(notes);
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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_note) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_setting) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     * @param position -1时表示新数据
     */
    private void startNotesAtyForResult(int position) {
        Intent intent = new Intent(mContext, NotesActivity.class);
        if (position != NotesActivity.POSITION_NEW_NOTES) {

            Notes note = mAdapter.getNotesByPosition(position);
            intent.putExtra(NotesActivity.KEY_NOTES, note);
        }
        startActivityForResult(intent, NotesActivity.ADD_NOTES_REQUEST);


    }

}
