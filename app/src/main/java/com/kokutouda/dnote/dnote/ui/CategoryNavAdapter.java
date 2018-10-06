package com.kokutouda.dnote.dnote.ui;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.data.MainNavData;
import com.kokutouda.dnote.dnote.db.Category;

import java.util.ArrayList;
import java.util.List;

import static com.kokutouda.dnote.dnote.ui.RecyclerViewType.VIEW_TYPE_FOOTER;
import static com.kokutouda.dnote.dnote.ui.RecyclerViewType.VIEW_TYPE_HEADER;
import static com.kokutouda.dnote.dnote.ui.RecyclerViewType.VIEW_TYPE_MAIN;

public class CategoryNavAdapter extends RecyclerView.Adapter {

    private List<Category> mCategoryList;
    private List<MainNavData> mHeaderData;
    private List<MainNavData> mFooterData;

    private View.OnClickListener mItemClickListener;
    private View.OnLongClickListener mItemLongClickListener;
    private int mOldCheckedPosition = -1;

    interface AdapterCheckable extends Checkable {
        @Override
        public void setChecked(boolean checked);

        @Override
        public boolean isChecked();

        @Override
        public void toggle();

        public void setViewByChecked();
    }


    public class MainViewHolder extends RecyclerView.ViewHolder implements AdapterCheckable {
        ImageView imageIcon;
        TextView textName;
        TextView textCount;
        boolean checked;

        MainViewHolder(View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_nav_menu_item);
            textName = itemView.findViewById(R.id.text_nav_menu_item);
            textCount = itemView.findViewById(R.id.text_notes_category_nums);
        }

        @Override
        public void setChecked(boolean checked) {
            this.checked = checked;
            setViewByChecked();
        }

        @Override
        public boolean isChecked() {
            return this.checked;
        }

        @Override
        public void toggle() {
            setChecked(!this.checked);
        }

        @Override
        public void setViewByChecked() {
            if (isChecked()) {
                textName.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                textName.setTypeface(null, Typeface.BOLD);

            } else {
                textName.setTextColor(itemView.getResources().getColor(R.color.navDefault));
                textName.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public class HeaderFooterViewHolder extends RecyclerView.ViewHolder implements AdapterCheckable {
        ImageView imageIcon;
        TextView textName;
        private boolean checked;

        HeaderFooterViewHolder(View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_nav_menu_item);
            textName = itemView.findViewById(R.id.text_nav_menu_item);
        }

        @Override
        public void setChecked(boolean checked) {
            this.checked = checked;
            setViewByChecked();
        }

        @Override
        public boolean isChecked() {
            return this.checked;
        }

        @Override
        public void toggle() {
            setChecked(!this.checked);
        }

        @Override
        public void setViewByChecked() {
            if (isChecked()) {
                Drawable drawable = DrawableCompat.wrap(imageIcon.getDrawable());
                DrawableCompat.setTint(imageIcon.getDrawable(), itemView.getResources().getColor(R.color.colorPrimary));

                textName.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                textName.setTypeface(null, Typeface.BOLD);
            } else {
                Drawable drawable = DrawableCompat.wrap(imageIcon.getDrawable());
                DrawableCompat.setTint(drawable, itemView.getResources().getColor(R.color.navDefault));
                textName.setTextColor(itemView.getResources().getColor(R.color.navDefault));
                textName.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public CategoryNavAdapter(@Nullable List<MainNavData> headerData
            , @Nullable List<MainNavData> footerData) {
        mHeaderData = headerData;
        mFooterData = footerData;
        if (mHeaderData == null) {
            mHeaderData = new ArrayList<>();
        }
        if (mFooterData == null) {
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_MAIN) {
            MainViewHolder main = (MainViewHolder) holder;
            int mainIndex = position - mHeaderData.size();
            Category category = mCategoryList.get(mainIndex);
            main.textName.setText(category.name);
            main.textCount.setText(String.valueOf(category.count));
            main.setChecked(main.isChecked());
        } else {
            HeaderFooterViewHolder headerFooter = (HeaderFooterViewHolder) holder;
            MainNavData item;
            if (position < mHeaderData.size()) {
                item = mHeaderData.get(position);
            } else {
                int footerIndex = position - mHeaderData.size() - mCategoryList.size();
                item = mFooterData.get(footerIndex);
            }
            headerFooter.imageIcon.setImageResource(item.getRes());
            headerFooter.setChecked(headerFooter.isChecked());
            headerFooter.textName.setText(item.getText());
        }

        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onClick(v);
                }
            });
        }
        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (holder instanceof HeaderFooterViewHolder) {
                        return false;
                    } else {
                        mItemLongClickListener.onLongClick(v);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            this.onBindViewHolder(holder, position);
        } else {
            if (payloads.get(0) instanceof RecyclerView.ViewHolder) {
                RecyclerView.ViewHolder oldCheckedViewHolder = (RecyclerView.ViewHolder) payloads.get(0);
                if (oldCheckedViewHolder instanceof AdapterCheckable) {
                    ((AdapterCheckable) oldCheckedViewHolder).setChecked(false);
                }
            }
            if (holder instanceof AdapterCheckable) {
                ((AdapterCheckable) holder).setChecked(true);
                mOldCheckedPosition = position;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCategoryList == null) {
            return 0;
        }
        return mHeaderData.size() + mCategoryList.size() + mFooterData.size();
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Category getItemByAdapterPosition(int position) {
        return this.mCategoryList.get(position);
    }

    public Category getItemByLayoutPosition(int position) {
        return this.mCategoryList.get(position - mHeaderData.size());
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(View.OnLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.mCategoryList = categoryList;
        notifyDataSetChanged();
    }

    public int getOldPosition() {
        return mOldCheckedPosition;
    }

    public void setOldPosition(int position) {
        notifyDataSetChanged();
        this.mOldCheckedPosition = position;
    }
}
