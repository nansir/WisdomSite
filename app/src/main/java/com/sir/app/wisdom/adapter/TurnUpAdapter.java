package com.sir.app.wisdom.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.model.entity.TurnUpBean;
import com.sir.library.base.BaseRecyclerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * 到場情況
 * Created by zhuyinan on 2020/5/13.
 */
public class TurnUpAdapter extends BaseRecyclerAdapter<TurnUpBean> {

    public TurnUpAdapter(@NonNull Activity context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_turn_up;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        TurnUpBean bean = getItem(position);
        holder.setText(R.id.tv_sub_name, bean.getSubcontractorName());

    }
}
