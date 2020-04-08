package com.sir.app.wisdom.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.R;
import com.sir.library.base.BaseRecyclerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * 车辆信息记录
 * Created by zhuyinan on 2020/4/8.
 */
public class VehicleAdapter extends BaseRecyclerAdapter<String> {

    public VehicleAdapter(@NonNull Activity context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_vehicle;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {

    }
}
