package com.kokutouda.dnote.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.kokutouda.dnote.R
import com.kokutouda.dnote.data.NoteDb
import org.jetbrains.anko.find
import kotlinx.android.synthetic.main.quick_edit_layout.*
import org.jetbrains.anko.startActivity

/**
 * Created by apple on 2017/7/5.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        val SAVE_NOTE_REQUEST = 100
    }

    private val mDrawer by lazy { find<DrawerLayout>(R.id.drawer_layout) }
    private val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    private val navigationView by lazy { find<NavigationView>(R.id.nav_view) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawer.addDrawerListener(toggle)
        //没有这个方法就显示不了 左上角的三条杠
        toggle.syncState()

        //todo 就没调用这个方法，为什么？
        navigationView.setNavigationItemSelectedListener {
            val id = it.itemId
            Log.d("MainActivity", "NavigatonItemSelected")
            when (id) {
                R.id.nav_note -> Log.d("MainActivity", "nav_not")
                R.id.nav_setting -> Log.d("MainActivity", "nav_setting")
            }
            mDrawer.closeDrawer(GravityCompat.START)
            true
        }

        textQuickEdit.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, SAVE_NOTE_REQUEST)
        }
    }

    //Receive result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            SAVE_NOTE_REQUEST -> saveNote(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun saveNote(data: Intent?) {
        var title = data?.getStringExtra(DetailActivity.TITLE)
        var content = data?.getStringExtra(DetailActivity.CONTENT)
        when {
            title == null -> title = ""
            content == null -> content = ""
        }
        NoteDb().saveNote(title!!, content!!)
        //todo adapter 提醒更新
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) mDrawer.closeDrawer(GravityCompat.START) else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean  {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //右上角的设置
    override fun onOptionsItemSelected(item: MenuItem): Boolean = with(item){
        when (itemId) {
            R.id.action_settings -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}
