package com.sir.app.wisdom.view;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.sir.app.wisdom.LoginActivity;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.CardFragmentPagerAdapter;
import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.TurnUpBean;
import com.sir.app.wisdom.view.card.ShadowTransformer;
import com.sir.app.wisdom.vm.PersonnelViewModel;
import com.sir.library.com.AppBaseActivity;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 劳务首页
 * Created by zhuyinan on 2020/4/8.
 */
public class MainPersonnelActivity extends AppActivity<PersonnelViewModel> {

    long mBeforeTouchTime;

//    @BindView(R.id.iv_personnel_record)
//    ViewPager ivPersonnelRecord;

    @Override
    public int bindLayout() {
        return R.layout.activity_main_personnel;
    }

    @Override
    public void doBusiness() {
//        CardFragmentPagerAdapter adapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), 6f);
//        ShadowTransformer transformer = new ShadowTransformer(ivPersonnelRecord, adapter);
//        transformer.enableScaling(false);
//        ivPersonnelRecord.setAdapter(adapter);
//        ivPersonnelRecord.setPageTransformer(true, transformer);
//        ivPersonnelRecord.setOffscreenPageLimit(3);

        //人员统计
        mViewModel.personnelRecords();

        //各分包商到场情况
        mViewModel.getPerson();
    }

    @Override
    protected void dataObserver() {

        //人员统计
        mViewModel.getPersonnelRecord().observe(this, new Observer<List<PersonnelRecordBean>>() {
            @Override
                public void onChanged(List<PersonnelRecordBean> list) {

                }
        });

        //各分包商到场情况
        mViewModel.getTurnUp().observe(this, new Observer<List<TurnUpBean>>() {
            @Override
            public void onChanged(List<TurnUpBean> turnUpBeans) {

            }
        });
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
