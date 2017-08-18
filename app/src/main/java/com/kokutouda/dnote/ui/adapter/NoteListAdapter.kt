package com.kokutouda.dnote.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kokutouda.dnote.R
import com.kokutouda.dnote.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * Created by apple on 2017/7/18.
 */
class NoteListAdapter(val list: List<Note>, val itemClick: (Note) -> Unit) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, true)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindNote(list[position])
    }

    class ViewHolder(view: View, val itemClick: (Note) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindNote(note: Note) {
            with(note) {
                itemView.textTitle.text = title
                itemView.textContent.text = content
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

}