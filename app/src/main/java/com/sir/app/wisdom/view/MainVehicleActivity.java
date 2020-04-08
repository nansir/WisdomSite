package com.sir.app.wisdom.view;

import android.view.View;

import com.sir.app.wisdom.LoginActivity;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.view.FaceRecognitionActivity;
import com.sir.app.wisdom.view.VehicleRecordActivity;
import com.sir.app.wisdom.view.VehicleUploadActivity;
import com.sir.library.com.AppBaseActivity;
import com.sir.library.com.AppLogger;

import butterknife.OnClick;

/**
 * 车闸首页
 * Created by zhuyinan on 2020/4/8.
 */
public class MainVehicleActivity extends AppBaseActivity {

    long mBeforeTouchTime;

    @Override
    public int bindLayout() {
        return R.layout.activity_main_vehicle;
    }

    @Override
    public void doBusiness() {


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

    private void singOut() {
        mDialog.showMessage("退出登录账户？");
        mDialog.setTitle("注销");
        mDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOperation.forward(LoginActivity.class);
                finish();
            }
        });
    }
}
