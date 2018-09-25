package com.kokutouda.dnote.dnote.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.db.Category;
import com.kokutouda.dnote.dnote.db.NotesRepository;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private int mResources;
    private Context mContext;
    private List<Category> mCategoryList;

    class ViewHolder {
        TextView textName;
        TextView textCount;
        ImageView image;

        ViewHolder(View v) {
            image = v.findViewById(R.id.image_notes_category_list);
            textName = v.findViewById(R.id.text_notes_category_name);
            textCount = v.findViewById(R.id.text_notes_category_nums);
        }

    }

    public CategoryAdapter(@NonNull Context context, int resource) {
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
        }
        ViewHolder viewHolder = new ViewHolder(convertView);

        Category item = (Category) getItem(position);
//todo 动态设置不同的分类颜色            viewHolder.image.setImageTintList();
        if (item != null) {
            viewHolder.textName.setText(item.name);
            viewHolder.textCount.setText(String.valueOf(item.count));
        }
        return convertView;
    }

    /*
    private int mResources;
    private Context mContext;
    private List<Category> mCategoryList;

    class ViewHolder {
        TextView textName;
        TextView textCount;
        ImageView image;

        public ViewHolder(View v) {
            image = v.findViewById(R.id.image_notes_category_list);
            textName = v.findViewById(R.id.text_notes_category_name);
            textCount = v.findViewById(R.id.text_notes_category_nums);
        }

    }

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);
        mResources = resource;
        mContext = context;
    }


    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        mResources = resource;
        mContext = context;

    }



    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(convertView, parent, position, mResources);
    }

    private @NonNull View createViewFromResource(@Nullable View convertView, @NonNull ViewGroup parent, int position, int resource) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(resource, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);

        T item = getItem(position);
        if (item instanceof Category) {

//todo 动态设置不同的分类颜色            viewHolder.image.setImageTintList();
            viewHolder.textName.setText(((Category) item).name);
            viewHolder.textCount.setText(String.valueOf(((Category) item).count));
        }

        return convertView;
    }
    */
}
