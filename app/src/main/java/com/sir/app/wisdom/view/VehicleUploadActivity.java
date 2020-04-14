package com.sir.app.wisdom.view;

import android.view.View;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.dialog.SubmitResultsDialog;
import com.sir.library.com.AppBaseActivity;

import butterknife.OnClick;

/**
 * 车辆信息上传
 * Created by zhuyinan on 2020/4/8.
 */
public class VehicleUploadActivity extends AppBaseActivity {

    SubmitResultsDialog resultsDialog;


    @Override
    public int bindLayout() {
        return R.layout.activity_vehicle_upload;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
        resultsDialog = new SubmitResultsDialog(this);
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                resultsDialog.show();
                resultsDialog.setSuccess();
                resultsDialog.setBackListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
        }
    }
}
