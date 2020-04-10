package com.sir.app.wisdom.view;

import android.view.View;

import com.sir.app.wisdom.LoginActivity;
import com.sir.app.wisdom.R;
import com.sir.library.com.AppBaseActivity;
import com.sir.library.com.AppLogger;

import butterknife.OnClick;

/**
 * 劳务首页
 * Created by zhuyinan on 2020/4/8.
 */
public class MainPersonnelActivity extends AppBaseActivity {

    long mBeforeTouchTime;

    @Override
    public int bindLayout() {
        return R.layout.activity_main_personnel;
    }

    @Override
    public void doBusiness() {


    }

    @OnClick({R.id.btn_login_out, R.id.tv_info_upload, R.id.tv_info_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_out:
                singOut();
                break;
            case R.id.tv_info_upload:
                mOperation.forward(PersonnelUploadActivity.class);
                break;
            case R.id.tv_info_record:
                mOperation.forward(PersonnelRecordActivity.class);
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
