package com.kokutouda.dnote.dnote.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.data.MainNavData;
import com.kokutouda.dnote.dnote.db.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryNavAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_HEADER = -1;
    private static final int VIEW_TYPE_FOOTER = -2;
    private static final int VIEW_TYPE_MAIN = 0;


    private List<Category> mCategoryList;
    private List<MainNavData> mHeaderData;
    private List<MainNavData> mFooterData;

    private class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIcon;
        TextView textName;
        TextView textCount;

        MainViewHolder(View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_nav_menu_item);
            textName = itemView.findViewById(R.id.text_nav_menu_item);
            textCount = itemView.findViewById(R.id.text_notes_category_nums);
        }
    }

    private class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIcon;
        TextView textName;

        HeaderFooterViewHolder(View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_nav_menu_item);
            textName = itemView.findViewById(R.id.text_nav_menu_item);
        }
    }

    public CategoryNavAdapter(@Nullable List<MainNavData> headerData, @Nullable List<MainNavData> footerData) {
        this.mHeaderData = headerData;
        this.mFooterData = footerData;
        if (this.mHeaderData == null) {
            mHeaderData = new ArrayList<>();
        }
        if (this.mFooterData == null) {
            mFooterData = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MAIN) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category_main, parent, false);
            return new MainViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category_header_footer, parent, false);
            return new HeaderFooterViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainViewHolder) {
            MainViewHolder main = (MainViewHolder) holder;
            int mainIndex = position - mHeaderData.size();
            Category category = mCategoryList.get(mainIndex);
//        holder.imageIcon = mCategoryList.
            main.textName.setText(category.name);
            main.textCount.setText(String.valueOf(category.count));

        } else if (holder instanceof HeaderFooterViewHolder) {
            HeaderFooterViewHolder headerFooter = (HeaderFooterViewHolder) holder;
            MainNavData item;
            if (position < mHeaderData.size()) {
                item = mHeaderData.get(position);

            } else {
                int footerIndex = position - mHeaderData.size() - mCategoryList.size();
                item = mFooterData.get(footerIndex);
            }
            headerFooter.imageIcon.setImageResource(item.getRes());
            headerFooter.textName.setText(item.getText());
        }

    }

    @Override
    public int getItemCount() {
        if (mCategoryList == null) {
            return 0;
        }
        return mCategoryList.size() + mHeaderData.size() + mFooterData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderData.size()) {
            return VIEW_TYPE_HEADER;
        } else if (position >= mHeaderData.size() + mCategoryList.size()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_MAIN;
        }
    }

    public void setCategoryList(List<Category> categoryList) {
        this.mCategoryList = categoryList;
        notifyDataSetChanged();
    }
}
