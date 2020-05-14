package com.sir.app.wisdom.adapter;

import android.app.Activity;
import android.widget.ProgressBar;

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

        holder.setText(R.id.tv_sub_record_dali, "(" + bean.getDaodali() + "/" + bean.getCountdali() + ")");
        holder.setText(R.id.tv_sub_record_safe, "(" + bean.getDaoSafe() + "/" + bean.getCountanquan() + ")");
        ProgressBar dali = holder.getView(R.id.pb_sub_record_dali);
        ProgressBar safe = holder.getView(R.id.pb_sub_record_safe);
        dali.setMax(bean.getCountdali());
        dali.setProgress(bean.getDaodali());
        safe.setMax(bean.getCountanquan());
        safe.setProgress(bean.getDaoSafe());
    }
}
