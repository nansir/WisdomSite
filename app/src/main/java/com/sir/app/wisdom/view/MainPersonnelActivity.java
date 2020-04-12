package com.sir.app.wisdom.view;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.sir.app.wisdom.LoginActivity;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.CardFragmentPagerAdapter;
import com.sir.app.wisdom.view.card.ShadowTransformer;
import com.sir.library.com.AppBaseActivity;
import com.sir.library.com.AppLogger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 劳务首页
 * Created by zhuyinan on 2020/4/8.
 */
public class MainPersonnelActivity extends AppBaseActivity {

    long mBeforeTouchTime;

    @BindView(R.id.iv_personnel_record)
    ViewPager ivPersonnelRecord;

    @Override
    public int bindLayout() {
        return R.layout.activity_main_personnel;
    }

    @Override
    public void doBusiness() {
        CardFragmentPagerAdapter adapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), 6f);
        ShadowTransformer transformer = new ShadowTransformer(ivPersonnelRecord, adapter);
        transformer.enableScaling(false);
        ivPersonnelRecord.setAdapter(adapter);
        ivPersonnelRecord.setPageTransformer(true, transformer);
        ivPersonnelRecord.setOffscreenPageLimit(3);
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
