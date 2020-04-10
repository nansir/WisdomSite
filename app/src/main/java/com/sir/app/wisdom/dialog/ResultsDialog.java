package com.sir.app.wisdom.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.R;
import com.sir.library.base.BaseDialog;

/**
 * 提交信息結果
 * Created by zhuyinan on 2020/4/8.
 */
public class ResultsDialog extends BaseDialog {

    public ResultsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.dialog_results;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setCancelable(false);
    }

    @Override
    public void doBusiness() {

    }

    public void setSuccess() {
        mViewHelper.setTextVal(R.id.tv_results_tile, "信息提交成功");
        mViewHelper.setTextVal(R.id.tv_results_hint, "信息提交成功，3S後自動跳轉回首頁");
        ImageView icon = findViewById(R.id.iv_results_icon);
        icon.setImageResource(R.mipmap.ic_submit_success);
    }

    public void setFailure(String msg) {
        mViewHelper.setTextVal(R.id.tv_results_tile, "信息提交失敗");
        mViewHelper.setTextVal(R.id.tv_results_hint, msg);
        ImageView icon = findViewById(R.id.iv_results_icon);
        icon.setImageResource(R.mipmap.ic_submit_failure);
    }

    public void setBackListener(View.OnClickListener listener) {
        mViewHelper.setOnClickListener(R.id.btn_submit, listener);
    }
}
