package com.sir.app.wisdom.view;

import android.view.View;

import com.sir.app.wisdom.R;
import com.sir.library.com.AppBaseActivity;

import butterknife.OnClick;

/**
 * 人员信息上传
 * Created by zhuyinan on 2020/4/8.
 */
public class PersonnelUploadActivity extends AppBaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_personnel_upload;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:

                break;
            case R.id.btn_submit:

                break;
        }
    }
}
