package com.sir.app.wisdom.view;

import android.view.View;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.dialog.ResultsDialog;
import com.sir.library.com.AppBaseActivity;

import java.util.logging.Handler;

import butterknife.OnClick;

/**
 * 车辆信息上传
 * Created by zhuyinan on 2020/4/8.
 */
public class VehicleUploadActivity extends AppBaseActivity {

    ResultsDialog resultsDialog;


    @Override
    public int bindLayout() {
        return R.layout.activity_vehicle_upload;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
        resultsDialog = new ResultsDialog(this);
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
