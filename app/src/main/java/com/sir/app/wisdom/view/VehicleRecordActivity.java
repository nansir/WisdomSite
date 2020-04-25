package com.sir.app.wisdom.view;

import android.os.Handler;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.VehicleAdapter;
import com.sir.library.mvvm.HolderActivity;
import com.sir.library.refresh.OnRefreshListener;
import com.sir.library.refresh.holder.RecyclerSwipeViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆信息上传记录
 * Created by zhuyinan on 2020/4/8.
 */
public class VehicleRecordActivity extends HolderActivity<RecyclerSwipeViewHolder> implements OnRefreshListener {

    @Override
    public int bindLayout() {
        return R.layout.activity_vehicle_record;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);

        mViewHolder.setAdapter(new VehicleAdapter(getActivity()));
        mViewHolder.setOnRefreshListener(this);

        initData();
    }

    private void initData() {
       new Handler().postDelayed(() -> {
           List<String> list = new ArrayList<>();
           list.add("");
           list.add("");
           list.add("");
           list.add("");
           mViewHolder.loadData(list);
       },1000);
    }


    @Override
    public void onRefresh() {
        initData();
    }
}
