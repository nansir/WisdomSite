package com.sir.app.wisdom.view;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.sir.app.wisdom.LoginActivity;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.VehicleRecordAdapter;
import com.sir.app.wisdom.model.entity.AccessInfoBean;
import com.sir.app.wisdom.vm.VehicleViewModel;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 车闸首页
 * Created by zhuyinan on 2020/4/8.
 */
public class MainVehicleActivity extends AppActivity<VehicleViewModel> {

    long mBeforeTouchTime;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    VehicleRecordAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_main_vehicle;
    }

    @Override
    public void doBusiness() {
        adapter = new VehicleRecordAdapter(getActivity());
        mViewModel.getAccessInfo(1);
    }

    @Override
    protected void dataObserver() {
        mViewModel.getAccessInfo().observe(this, bean -> {
            adapter.addItem(bean);
            vpContent.setAdapter(adapter);
        });
    }

    @OnClick({R.id.btn_login_out, R.id.tv_info_upload, R.id.tv_info_record, R.id.tv_info_face})
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
}
