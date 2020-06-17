package com.sir.app.wisdom.view;

import androidx.lifecycle.Observer;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.VehicleAdapter;
import com.sir.app.wisdom.model.entity.VehicleRecordsBean;
import com.sir.app.wisdom.vm.VehicleViewModel;
import com.sir.library.mvvm.AppHolderActivity;
import com.sir.library.refresh.OnRefreshListener;
import com.sir.library.refresh.holder.RecyclerSwipeViewHolder;

import java.util.List;

import butterknife.OnClick;

/**
 * 车辆进出记录
 * Created by zhuyinan on 2020/4/8.
 */
public class VehicleRecordActivity extends AppHolderActivity<VehicleViewModel, RecyclerSwipeViewHolder> implements OnRefreshListener {

    @Override
    public int bindLayout() {
        return R.layout.activity_vehicle_record;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);

        mViewHolder.setAdapter(new VehicleAdapter(getActivity()));
        mViewHolder.setEmptyMsg("暫無車輛進入記錄");
        mViewHolder.setOnRefreshListener(this);
        mViewModel.vehicleRecords("");
    }

    @Override
    protected void dataObserver() {
        mViewModel.getVehicleRecords().observe(this, new Observer<List<VehicleRecordsBean>>() {
            @Override
            public void onChanged(List<VehicleRecordsBean> list) {
                mViewHolder.adapter.clearAllItem();
                mViewHolder.loadData(list);
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        String key = mViewHelper.getEditVal(R.id.et_search);
        mViewModel.vehicleRecords(key);
    }

    @Override
    public void onRefresh() {
        mViewModel.vehicleRecords("");
    }
}
