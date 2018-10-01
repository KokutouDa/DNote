package com.kokutouda.dnote.dnote.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static com.kokutouda.dnote.dnote.ui.RecyclerViewType.VIEW_TYPE_MAIN;

public class SimpleOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;
    private GestureDetector mGestureDetector;
    private RecyclerView mRecyclerView;
    private boolean mIsLongClick = false;
    //长按时选中的View
    private View mViewOnLongClick;


    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    public SimpleOnItemTouchListener(Context context, final RecyclerView recyclerView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        mClickListener = onItemClickListener;
        mRecyclerView = recyclerView;
        mLongClickListener = onItemLongClickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                mIsLongClick = false;
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View itemView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerView.getChildLayoutPosition(itemView);
                int viewType = mRecyclerView.getAdapter().getItemViewType(position);
                if (viewType == VIEW_TYPE_MAIN) {
                    mLongClickListener.onItemLongClick(itemView, position);
                } else {
                    mIsLongClick = true;
                    mViewOnLongClick = itemView;
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View itemView = rv.findChildViewUnder(e.getX(), e.getY());
        if (mClickListener != null && itemView != null && mGestureDetector.onTouchEvent(e)) {
            int position = rv.getChildLayoutPosition(itemView);
            mClickListener.onItemClick(itemView, position);
            return true;
        }
        if (mLongClickListener != null && itemView != null && mIsLongClick && e.getAction() == MotionEvent.ACTION_UP) {
            mIsLongClick = false;
            if (mViewOnLongClick == itemView) {
                int position = mRecyclerView.getChildLayoutPosition(itemView);
                mClickListener.onItemClick(itemView, position);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
}
