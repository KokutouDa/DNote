package com.kokutouda.dnote.data

import android.util.Log
import com.kokutouda.dnote.model.Note
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by apple on 2017/7/19.
 */

class NoteDb(val dbHelper: DbHelper = DbHelper.instance) {

    //Parse local data
//    fun requestNote() : List<Note> = dbHelper.use {
//        val parser = classParser<Note>()
//        select(NoteTable.NAME).parseList()
//    }

    //save data to db
    fun saveNote(title: String, content: String)= dbHelper.use {
        insert(NoteTable.NAME, NoteTable.TITLE to title, NoteTable.CONTENT to content)

    }

    fun saveNote(note: Note) = dbHelper.use {
        with(note) {
            insert(NoteTable.NAME, NoteTable.TITLE to title, NoteTable.CONTENT to content)
        }
    }
}