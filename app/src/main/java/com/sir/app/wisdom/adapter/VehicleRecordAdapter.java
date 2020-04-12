package com.sir.app.wisdom.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.R;
import com.sir.library.base.BasePagerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * Created by zhuyinan on 2020/4/12.
 */
public class VehicleRecordAdapter extends BasePagerAdapter<String> {

    public VehicleRecordAdapter(@NonNull Activity context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_vehicle_record;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {

    }
}
