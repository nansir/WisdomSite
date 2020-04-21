package com.sir.app.wisdom.adapter;

import android.app.Activity;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.model.entity.AccessInfoBean;
import com.sir.library.base.BasePagerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * Created by zhuyinan on 2020/4/12.
 */
public class VehicleRecordAdapter extends BasePagerAdapter<AccessInfoBean> {

    public VehicleRecordAdapter(@NonNull Activity context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_vehicle_record;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        AccessInfoBean bean = getItem(position);
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
                .into(headstock);
        //车顶
        Glide.with(mContext)
                .load(bean.getCarRoof())
                .into(carRoof);
    }
}
