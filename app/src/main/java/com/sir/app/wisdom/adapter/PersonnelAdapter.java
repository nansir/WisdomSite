package com.sir.app.wisdom.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
import com.sir.app.wisdom.view.PersonnelUploadActivity;
import com.sir.library.base.BaseRecyclerAdapter;
import com.sir.library.base.help.ViewHolder;

/**
 * 人员信息记录
 * Created by zhuyinan on 2020/4/8.
 */
public class PersonnelAdapter extends BaseRecyclerAdapter<RecordPersonnelBean> {

    public PersonnelAdapter(@NonNull Activity context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.adapter_personnel;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        RecordPersonnelBean bean = getItem(position);
        holder.setText(R.id.tv_info_plate, "姓名：" + bean.getCN_FullName());
        holder.setText(R.id.tv_info_name, "分判商：" + bean.getSubcontractorName());
        holder.setText(R.id.tv_info_number, "員工編號：" + bean.getStaffCode());
        holder.setText(R.id.tv_info_time, bean.getTime());

        ImageView head = holder.getView(R.id.iv_info_head);
        Glide.with(mContext)
                .load(bean.getPhoto())
                .placeholder(R.mipmap.ic_placeholder)//占位图片
                .into(head);

        //编辑
        holder.setOnClickListener(R.id.tv_info_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonnelUploadActivity.class);
                intent.putExtra(AppKey.ValA, bean);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void addItem(RecordPersonnelBean data) {
        mDataList.add(data);
        notifyDataSetChanged();
    }
}
