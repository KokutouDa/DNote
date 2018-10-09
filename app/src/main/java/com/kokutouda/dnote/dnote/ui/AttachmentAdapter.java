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


    public AttachmentAdapter() { }

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
        ImageView imageView;
        if (convertView == null) {
            //todo 一列转两列的时候有个显示上的bug
            Context context = parent.getContext();
            imageView = new ImageView(context);
            String fileName = mAttachments.get(position).fileName;
            Bitmap bitmap = BitmapUtils.getBitmap(context, fileName);

            int width = parent.getWidth() / 2;
            imageView.setLayoutParams(new ViewGroup.LayoutParams(width, width));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);

        } else {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }
}
