package com.kokutouda.dnote.dnote.ui;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public class NotesListOnItemTouchListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mClickListener;

    public NotesListOnItemTouchListener(OnItemClickListener onItemClickListener) {
        mClickListener = onItemClickListener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            View itemView = rv.findChildViewUnder(e.getX(), e.getY());
            if (mClickListener != null && itemView != null) {
                mClickListener.onItemClick(itemView);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {
        void onItemClick(View v);
    }
}
