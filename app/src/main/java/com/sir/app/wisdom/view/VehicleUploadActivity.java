package com.sir.app.wisdom.view;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.dialog.DatePickerDialog;
import com.sir.app.wisdom.dialog.SubmitResultsDialog;
import com.sir.app.wisdom.model.VehicleModel;
import com.sir.app.wisdom.model.entity.SubcontractorBean;
import com.sir.app.wisdom.model.entity.VehicleInfoBean;
import com.sir.app.wisdom.model.entity.VehicleTypeBean;
import com.sir.app.wisdom.vm.VehicleViewModel;
import com.sir.library.mvvm.AppActivity;
import com.sir.library.retrofit.event.ResState;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 车辆信息上传
 * Created by zhuyinan on 2020/4/8.
 */
public class VehicleUploadActivity extends AppActivity<VehicleViewModel> {

    SubmitResultsDialog resultsDialog;
    @BindView(R.id.s_vehicle_type)
    Spinner sVehicleType;
    @BindView(R.id.s_vehicle_subcontractor)
    Spinner sVehicleSubcontractor;
    ArrayAdapter adapterSubcontractor;
    ArrayAdapter adapterVehicleType;
    List<SubcontractorBean> subs;
    List<VehicleTypeBean> vehicleTypes;
    VehicleInfoBean infoBean;

    @Override
    public int bindLayout() {
        return R.layout.activity_vehicle_upload;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
        resultsDialog = new SubmitResultsDialog(this);
        infoBean = new VehicleInfoBean();
        mViewModel.subcontractor("");
        mViewModel.vehicleType();

        adapterVehicleType = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.tv_item);
        sVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                infoBean.setCardType(vehicleTypes.get(i).getTerritoryID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapterSubcontractor = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.tv_item);
        sVehicleSubcontractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                infoBean.setSubcontractorID(subs.get(i).getSubcontractorID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick({R.id.btn_submit, R.id.tv_vehicle_effective})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                submitVehicle();
                break;
            case R.id.tv_vehicle_effective:
                DatePickerDialog dialog = new DatePickerDialog(getActivity());
                dialog.show();
                dialog.setDatePicker(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String str = "%s年%s月%s日";
                        String time = "%s-%s-%s 00:00:00";
                        mViewHelper.setTextVal(R.id.tv_vehicle_effective, String.format(str, year, monthOfYear, dayOfMonth));
                        infoBean.setValidUntil(String.format(time, year, monthOfYear, dayOfMonth));
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void submitVehicle() {
        String vehicleOn = mViewHelper.getEditVal(R.id.et_vehicle_on);
        String vehiclePeople = mViewHelper.getEditVal(R.id.et_vehicle_people);
        String vehicleSize = mViewHelper.getEditVal(R.id.et_vehicle_size);
        String vehicleWeight = mViewHelper.getEditVal(R.id.et_vehicle_weight);

        if (TextUtils.isEmpty(vehicleOn)) {
            mDialog.showError("未填寫車牌號");
        } else if (TextUtils.isEmpty(vehiclePeople)) {
            mDialog.showError("未填寫車輛尺寸");
        } else if (TextUtils.isEmpty(vehicleSize)) {
            mDialog.showError("未填寫載重量");
        } else if (TextUtils.isEmpty(vehicleWeight)) {
            mDialog.showError("未填寫核載人數");
        } else {
            infoBean.setCar_No(vehicleOn);
            infoBean.setSize(vehicleSize);
            infoBean.setDeadweight(vehicleWeight);
            infoBean.setNumberNuclearCarriers(vehiclePeople);
            mViewModel.vehicleAction(infoBean);
        }
    }

    @Override
    protected void dataObserver() {
        //分包商
        mViewModel.getSubcontractor().observe(this, new Observer<List<SubcontractorBean>>() {
            @Override
            public void onChanged(List<SubcontractorBean> list) {
                subs = list;
                for (SubcontractorBean bean : list) {
                    adapterSubcontractor.add(bean.getSubcontractorName());
                }
                sVehicleSubcontractor.setAdapter(adapterSubcontractor);
            }
        });
        //车辆类型
        mViewModel.getVehicleType().observe(this, new Observer<List<VehicleTypeBean>>() {
            @Override
            public void onChanged(List<VehicleTypeBean> list) {
                vehicleTypes = list;
                for (VehicleTypeBean bean : list) {
                    adapterVehicleType.add(bean.getCar_Type_Name());
                }
                sVehicleType.setAdapter(adapterVehicleType);
            }
        });
    }

    @Override
    protected void notification(ResState state) {
        mDialog.dismiss();
        if (state.getCode() == VehicleModel.ON_SUCCESS) {
            resultsDialog.show();
            resultsDialog.setSuccess();
            resultsDialog.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            //三秒后自动关闭
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        } else if (state.getCode() == VehicleModel.ON_FAILURE) {
            resultsDialog.show();
            resultsDialog.setFailure(state.getMsg());
            resultsDialog.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resultsDialog.dismiss();
                }
            });
        } else {
            super.notification(state);
        }
    }
}
