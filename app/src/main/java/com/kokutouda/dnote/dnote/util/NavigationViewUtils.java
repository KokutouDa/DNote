package com.kokutouda.dnote.dnote.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.data.MainNavData;

import java.util.ArrayList;
import java.util.List;

public class NavigationViewUtils {

    public static List<MainNavData> getHeaderData(@NonNull Context context) {
        List<MainNavData> headerData = new ArrayList<>();
        headerData.add(new MainNavData(R.drawable.ic_nav_note_add, context.getString(R.string.nav_note)));
        headerData.add(new MainNavData(R.drawable.ic_nav_note_add, context.getString(R.string.all_recycler)));
        return headerData;
    }

    public static List<MainNavData> getFooterData(@NonNull Context context) {
        List<MainNavData> footerData = new ArrayList<>();
        footerData.add(new MainNavData(R.drawable.ic_nav_setting, context.getString(R.string.nav_setting)));
        return footerData;
    }
}
