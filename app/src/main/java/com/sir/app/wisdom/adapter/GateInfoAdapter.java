package com.sir.app.wisdom.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.model.entity.GateBean;
import com.sir.library.base.BaseRecyclerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * Created by zhuyinan on 2020/4/30.
 */
public class GateInfoAdapter extends BaseRecyclerAdapter<GateBean> {

    public GateInfoAdapter(@NonNull Activity context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_gate_info;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        GateBean bean = getItem(position);
        holder.setText(R.id.btn_gate, bean.getName());
    }
}
