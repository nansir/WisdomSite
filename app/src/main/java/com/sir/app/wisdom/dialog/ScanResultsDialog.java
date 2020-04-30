package com.sir.app.wisdom.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.GateInfoAdapter;
import com.sir.app.wisdom.model.entity.ResponseFaceBean;
import com.sir.app.wisdom.utils.TimeUtils;
import com.sir.library.base.BaseDialog;

/**
 * 人脸识别结果
 * Created by zhuyinan on 2020/4/14.
 */
public class ScanResultsDialog extends BaseDialog {

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
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //在setContentView之后调用
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void doBusiness() {

    }

    /**
     * @param bean
     */
    public void loadData(ResponseFaceBean bean, GateInfoAdapter adapter) {
        ImageView photo = findViewById(R.id.iv_personnel_photo);
        //圖片
        Glide.with(getContext())
                .load(bean.getPhoto())
                .placeholder(R.mipmap.ic_placeholder)//占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo);
        mViewHelper.setTextVal(R.id.tv_personnel_id, "員工編號：" + bean.getStaffID());
        mViewHelper.setTextVal(R.id.tv_personnel_name, "員工姓名：" + bean.getCN_FullName());
        mViewHelper.setTextVal(R.id.tv_personnel_sex, "性別：男");
        mViewHelper.setTextVal(R.id.tv_personnel_subc, "所屬分包商：" + bean.getSubcontractor());
        mViewHelper.setTextVal(R.id.tv_personnel_enter, "入場時間：" + TimeUtils.getCurrentTime());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), adapter.getItemCount() == 1 ? 1 : 2));
        recyclerView.setAdapter(adapter);
    }
}
