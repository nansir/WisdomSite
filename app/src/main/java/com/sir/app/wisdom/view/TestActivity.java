package com.sir.app.wisdom.view;

import com.sir.app.wisdom.R;
import com.sir.library.com.AppBaseActivity;

/**
 * Created by zhuyinan on 2020/4/13.
 */
public class TestActivity extends AppBaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void doBusiness() {

    }

    @Override
    protected boolean isUseFullScreenMode() {
        return true;
    }
}
