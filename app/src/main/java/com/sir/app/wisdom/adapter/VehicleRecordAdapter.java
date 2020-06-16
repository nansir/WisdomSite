package com.sir.app.wisdom.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.dialog.PhotoZoomDialog;
import com.sir.app.wisdom.model.entity.VehicleRecordsBean;
import com.sir.library.base.BasePagerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * Created by zhuyinan on 2020/4/12.
 */
public class VehicleRecordAdapter extends BasePagerAdapter<VehicleRecordsBean> {

    PhotoZoomDialog photoZoom;

    public VehicleRecordAdapter(@NonNull Activity context) {
        super(context);
        photoZoom = new PhotoZoomDialog(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_vehicle_record;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        VehicleRecordsBean bean = getItem(position);
        holder.setText(R.id.tv_record_address, bean.getAddress());
        holder.setText(R.id.tv_record_car_no, "車牌號碼：" + bean.getCarNo());
        holder.setText(R.id.tv_record_subcontractor, "所屬分包商：" + bean.getSubcontractor());
        holder.setText(R.id.tv_record_capture_time, "入場時間：" + bean.getCaptureTime());
        holder.setText(R.id.tv_record_driver, "司機：unknown");

        ImageView carRoof = holder.getView(R.id.iv_record_roof);
        ImageView headstock = holder.getView(R.id.iv_record_plate);

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
