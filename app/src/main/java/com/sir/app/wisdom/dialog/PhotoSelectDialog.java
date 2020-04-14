package com.sir.app.wisdom.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.R;
import com.sir.library.base.BaseDialog;

/**
 * 照片選擇對話
 * Created by zhuyinan on 2020/4/14.
 */
public class PhotoSelectDialog extends BaseDialog {

    public PhotoSelectDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.dialog_photo;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //设置显示位置在底部
        getWindow().setGravity(Gravity.BOTTOM);
        //设置显示动画
        getWindow().getAttributes().windowAnimations = R.style.window_anim_bottom_style;
        //设置window背景，默认的背景会有Padding值。
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //在setContentView之后调用
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void doBusiness() {
        mViewHelper.setOnClickListener(R.id.tv_photo_cancel, v -> dismiss());
    }

    public void set(View.OnClickListener listener) {
        mViewHelper.setOnClickListener(R.id.tv_photo_camera, listener);
        mViewHelper.setOnClickListener(R.id.tv_photo_select, listener);
    }
}
