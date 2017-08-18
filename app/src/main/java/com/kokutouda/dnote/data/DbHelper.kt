package com.kokutouda.dnote.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.kokutouda.dnote.ui.App
import org.jetbrains.anko.db.*
/**
 * Created by apple on 2017/7/5.
 */
class DbHelper(ctx: Context = App.instance) : ManagedSQLiteOpenHelper(ctx, DbHelper.DB_NAME, null, DbHelper.DB_VERSION) {

    companion object {
        val DB_NAME = "notes.db"
        val DB_VERSION = 1
        val instance by lazy { DbHelper() }
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(NoteTable.NAME, true,
                NoteTable.ID to INTEGER + PRIMARY_KEY,
                NoteTable.TITLE to TEXT,
                NoteTable.CONTENT to TEXT,
                NoteTable.DATE to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(NoteTable.NAME, true)
        onCreate(db)
    }
}