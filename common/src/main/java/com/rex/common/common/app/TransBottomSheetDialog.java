package com.rex.common.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;
import android.view.Window;

import com.rex.common.utils.UiUtil;

public class TransBottomSheetDialog extends BottomSheetDialog {

    public TransBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public TransBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected TransBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window  = getWindow();
        if(window == null){
            return;
        }

        // 得到屏幕高度
        int screenHeight = UiUtil.getScreenHeight(getOwnerActivity());
        // 得到状态栏的高度
        int statusHeight = UiUtil.getStatusBarHeight(getOwnerActivity());

        // 计算dialog的高度并设置
        int dialogHeight = screenHeight - statusHeight;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

    }
}
