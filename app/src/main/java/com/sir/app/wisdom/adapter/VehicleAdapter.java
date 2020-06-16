package com.sir.app.wisdom.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.dialog.PhotoZoomDialog;
import com.sir.app.wisdom.model.entity.VehicleRecordsBean;
import com.sir.library.base.BaseRecyclerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * 车辆信息记录
 * Created by zhuyinan on 2020/4/8.
 */
public class VehicleAdapter extends BaseRecyclerAdapter<VehicleRecordsBean> {

    PhotoZoomDialog photoZoom;

    public VehicleAdapter(@NonNull Activity context) {
        super(context);
        photoZoom = new PhotoZoomDialog(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_vehicle;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        VehicleRecordsBean bean = getItem(position);
        holder.setText(R.id.tv_info_time, bean.getRecordDate());
        holder.setText(R.id.tv_info_address, "出入口：" + bean.getAddress());
        holder.setText(R.id.tv_info_name, "分判商：" + bean.getSubcontractorName());
        holder.setText(R.id.tv_info_plate, "車牌：" + bean.getCar_No());


        ImageView carRoof = holder.getView(R.id.iv_record_roof);
        ImageView headstock = holder.getView(R.id.iv_record_plate);

        carRoof.setVisibility(TextUtils.isEmpty(bean.getHeadstock()) ? View.INVISIBLE : View.VISIBLE);
        holder.setVisible(R.id.iv_record_plate, !TextUtils.isEmpty(bean.getCarRoof()));

        //车牌
        Glide.with(mContext)
                .load(bean.getHeadstock())
                .placeholder(R.mipmap.ic_placeholder)//占位图片
                .into(headstock);
        //车顶
        Glide.with(mContext)
                .load(bean.getCarRoof())
                .placeholder(R.mipmap.ic_placeholder)//占位图片
                .into(carRoof);

        headstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoZoom.show();
                photoZoom.setImage(bean.getHeadstock_Big());
            }
        });

        carRoof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoZoom.show();
                photoZoom.setImage(bean.getCarRoof_Big());
            }
        });
    }
}
