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
import com.sir.library.com.AppLogger;

/**
 * 人脸识别结果
 * Created by zhuyinan on 2020/4/14.
 */
public class ScanResultsDialog extends BaseDialog implements View.OnClickListener {

    public ScanResultsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.dialog_results_scan;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //设置显示位置在底部
        getWindow().setGravity(Gravity.BOTTOM);
        //设置window背景，默认的背景会有Padding值。
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //在setContentView之后调用
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void doBusiness() {
        mViewHelper.setOnClickListener(R.id.btn_gate_a, this);
        mViewHelper.setOnClickListener(R.id.btn_gate_b, this);
        mViewHelper.setOnClickListener(R.id.btn_gate_c, this);
        mViewHelper.setOnClickListener(R.id.btn_gate_d, this);
    }

    @Override
    public void onClick(View v) {
        AppLogger.d("开启");
        dismiss();
    }
}
