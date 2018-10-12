package com.kokutouda.dnote.dnote.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Attachment;
import com.kokutouda.dnote.dnote.util.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class AttachmentAdapter extends BaseAdapter {

    private static final String TAG = "AttachmentAdapter";

    private List<Attachment> mAttachments;

    private int mNumColumns;


    public AttachmentAdapter(int numColumns) {
        mNumColumns = numColumns;
        if (mNumColumns < 1) {
            mNumColumns = 1;
        }
    }

    @Override
    public int getCount() {
        if (mAttachments != null) {
            return mAttachments.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mAttachments.get(position);
    }

    public void setAttachments(List<Attachment> attachments) {
        mAttachments = attachments;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_grid_view, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            String fileName = mAttachments.get(position).fileName;
            Bitmap bitmap = BitmapUtils.getBitmap(fileName);
            holder.imageView.setImageBitmap(bitmap);
        }
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = parent.getMeasuredWidth() / mNumColumns;
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }

    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
    }

    public class ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            imageView = itemView.findViewById(R.id.image_grid_view);
        }
    }
}
