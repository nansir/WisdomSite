package com.sir.app.wisdom.view;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.sir.app.wisdom.LoginActivity;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.VehicleRecordAdapter;
import com.sir.app.wisdom.model.VehicleModel;
import com.sir.app.wisdom.model.entity.StatisticsBean;
import com.sir.app.wisdom.view.holder.MainVehicleHolder;
import com.sir.app.wisdom.vm.VehicleViewModel;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppHolderActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 车闸首页
 * Created by zhuyinan on 2020/4/8.
 */
public class MainVehicleActivity extends AppHolderActivity<VehicleViewModel, MainVehicleHolder> implements RadioGroup.OnCheckedChangeListener {

    long mBeforeTouchTime;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    VehicleRecordAdapter adapter;
    @BindView(R.id.rg_data)
    RadioGroup rgData;
    @BindView(R.id.rg_vehicle_type)
    RadioGroup rgVehicleType;

    boolean flagType = true;
    int carType = 0; //车辆类型ID

    @Override
    public int bindLayout() {
        return R.layout.activity_main_vehicle;
    }

    @Override
    public void doBusiness() {
        adapter = new VehicleRecordAdapter(getActivity());
//        mViewModel.getAccessInfo(1); //进出情况
        mViewModel.totalVehicles();//本月进入车辆总数
//        mViewModel.GetAllCarType();//获取所以车辆类型


        //图表日期选择
        rgData.setOnCheckedChangeListener(this);
        rgVehicleType.setOnCheckedChangeListener(this);
    }


    @Override
    protected void dataObserver() {
        //选择的闸口获取车辆信息
        mViewModel.getAccessInfo().observe(this, bean -> {
            adapter.addItem(bean);
            vpContent.setAdapter(adapter);
        });

        //当月车辆进入总数
        mViewModel.subscribe(VehicleModel.EVENT_VEHICLES_TOTAL, List.class)
                .observe(this, new Observer<List>() {
                    @Override
                    public void onChanged(List list) {

                    }
                });

        //所有车辆类别
        mViewModel.subscribe(VehicleModel.EVENT_VEHICLE_TYPE, List.class)
                .observe(this, new Observer<List>() {
                    @Override
                    public void onChanged(List list) {
                        mViewHolder.setVehicleType(list);
                    }
                });

        //统计信息
        mViewModel.getStatistics().observe(this, new Observer<List<StatisticsBean>>() {
            @Override
            public void onChanged(List<StatisticsBean> statisticsBeans) {

            }
        });
    }

    @OnClick({R.id.btn_login_out, R.id.tv_info_upload, R.id.tv_info_record, R.id.tv_info_face, R.id.tv_vehicle_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_out:
                singOut();
                break;
            case R.id.tv_info_face:
                mOperation.forward(FaceRecognitionActivity.class);
                break;
            case R.id.tv_info_upload:
                mOperation.forward(VehicleUploadActivity.class);
                break;
            case R.id.tv_info_record:
                mOperation.forward(VehicleRecordActivity.class);
                break;
            case R.id.tv_vehicle_type:
                mViewHelper.setVisibility(R.id.rg_vehicle_type, flagType);
                flagType = !flagType;
                break;
        }
    }

    private void singOut() {
        mDialog.showMessage("退出登錄賬戶？");
        mDialog.setTitle("註銷");
        mDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOperation.forward(LoginActivity.class);
                finish();
            }
        });
    }

    @Override
    protected boolean isUseFullScreenMode() {
        return true;
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - mBeforeTouchTime) >= 2000) {
            AppLogger.toast(R.string.press_exit);
            mBeforeTouchTime = currentTime;
        } else {
            finish();
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.rg_data) { //选择日期

            switch (checkedId) {
                case R.id.rbtn_day://1.天，2.周，3.月
                    mViewModel.statistics(carType, 1, 1);
                    break;
                case R.id.rbtn_week://1.天，2.周，3.月
                    mViewModel.statistics(carType, 2, 1);
                    break;
                case R.id.rbtn_month://1.天，2.周，3.月
                    mViewModel.statistics(carType, 3, 1);
                    break;
            }
        } else if (group.getId() == R.id.rg_vehicle_type) {//选择车辆类型
            RadioButton rbtn = findViewById(checkedId);
            mViewHelper.setTextVal(R.id.tv_vehicle_type, rbtn.getText());
            carType = (int) rbtn.getTag(); //车辆类型
            //请求数据
            mViewModel.statistics(carType, 1, 1);//图表统计
        }
    }
}
