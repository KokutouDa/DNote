package com.kokutouda.dnote.dnote.util;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.kokutouda.dnote.dnote.DialogErrorBinding;
import com.kokutouda.dnote.dnote.R;
import com.kokutouda.dnote.dnote.ui.SimpleTextWatcher;

public class DialogUtil {

    public static AlertDialog createCategoryDialog(Context context, View view, @Nullable final String categoryName) {
        final DialogErrorBinding dialogErrorBinding = DataBindingUtil.bind(view);
        dialogErrorBinding.setIsError(new ObservableBoolean(false));
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogEditTheme);
        final AlertDialog alertDialog = builder.setTitle(R.string.dialog_edit_category)
                .setView(view)
                .create();
        EditText editTextCategoryName = view.findViewById(R.id.edit_dialog);
        if (categoryName != null) {
            editTextCategoryName.setText(categoryName);
            editTextCategoryName.setSelection(categoryName.length());
        }
        editTextCategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (categoryName == null && hasFocus) {
                    setDialogButtonClickable(alertDialog, DialogInterface.BUTTON_POSITIVE, false);
                }
            }
        });
        editTextCategoryName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean clickable = !s.toString().equals("");
                setDialogButtonClickable(alertDialog, DialogInterface.BUTTON_POSITIVE, clickable);
                if (clickable) {
                    dialogErrorBinding.getIsError().set(false);
                }
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!isDialogButtonClickable(alertDialog, DialogInterface.BUTTON_POSITIVE)) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                dialogErrorBinding.getIsError().set(true);
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        return alertDialog;
    }

    public static boolean isDialogButtonClickable(AlertDialog dialog, int witchButton) {
        return dialog.getButton(witchButton).isClickable();
    }

    public static void setDialogButtonClickable(AlertDialog dialog, int witchButton, boolean clickable) {
        dialog.getButton(witchButton).setClickable(clickable);
    }
}
