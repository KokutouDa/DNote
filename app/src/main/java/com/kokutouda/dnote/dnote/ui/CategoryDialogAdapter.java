package com.kokutouda.dnote.dnote.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Category;

import java.util.List;

public class CategoryDialogAdapter extends BaseAdapter {

    private int mResources;
    private Context mContext;
    private List<Category> mCategoryList;

    class ViewHolder {
        TextView textName;
        TextView textCount;
        ImageView imageIcon;

        ViewHolder(View v) {
            imageIcon = v.findViewById(R.id.image_nav_menu_item);
            textName = v.findViewById(R.id.text_nav_menu_item);
            textCount = v.findViewById(R.id.text_notes_category_nums);
        }

    }

    public CategoryDialogAdapter(@NonNull Context context, int resource) {
        mResources = resource;
        mContext = context;
    }

    public void setCategoryList(List<Category> categoryList) {
        mCategoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mCategoryList != null) {
            return mCategoryList.size();
        }
        return 0;
    }

    @Override
    public @Nullable Object getItem(int position) {
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(convertView, parent, position, mResources);
    }

    private @NonNull View createViewFromResource(@Nullable View convertView, @NonNull ViewGroup parent, int position, int resource) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(resource, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);

            Category item = (Category) getItem(position);
            if (item != null) {
                viewHolder.textName.setText(item.name);
                viewHolder.textCount.setText(String.valueOf(item.count));
            }
        }

        return convertView;
    }
}