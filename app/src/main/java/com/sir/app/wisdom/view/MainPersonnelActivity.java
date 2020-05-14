package com.sir.app.wisdom.view;

import android.view.View;

import androidx.lifecycle.Observer;

import com.sir.app.wisdom.LoginActivity;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.TurnUpAdapter;
import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.TurnUpBean;
import com.sir.app.wisdom.view.weight.ProgressView;
import com.sir.app.wisdom.vm.PersonnelViewModel;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppHolderActivity;
import com.sir.library.refresh.holder.RecyclerStateViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 劳务首页
 * Created by zhuyinan on 2020/4/8.
 */
public class MainPersonnelActivity extends AppHolderActivity<PersonnelViewModel, RecyclerStateViewHolder> {

    long mBeforeTouchTime;
    @BindView(R.id.pv_jinkai)
    ProgressView pvJinkai;
    @BindView(R.id.pv_yinwai)
    ProgressView pvYinwai;
    @BindView(R.id.pv_enter)
    ProgressView pvEnter;

    @Override
    public int bindLayout() {
        return R.layout.activity_main_personnel;
    }

    @Override
    public void doBusiness() {
        mViewHolder.setAdapter(new TurnUpAdapter(getActivity()));
        mViewHolder.swipeRecyclerView.setNestedScrollingEnabled(false);
        //各分包商到场情况
        mViewModel.getPerson();
        //人员统计
        mViewModel.personnelRecords();
    }

    @Override
    protected void dataObserver() {
        //人员统计
        mViewModel.getPersonnelRecord().observe(this, new Observer<List<PersonnelRecordBean>>() {
            @Override
            public void onChanged(List<PersonnelRecordBean> list) {
                updateTurnUp(list.get(0));

//                NestedScrollView svscrollouter = findViewById(R.id.nsv);
//                svscrollouter.fullScroll(NestedScrollView.FOCUS_UP);
            }
        });

        //各分包商到场情况
        mViewModel.getTurnUp().observe(this, new Observer<List<TurnUpBean>>() {
            @Override
            public void onChanged(List<TurnUpBean> turnUpBeans) {
                mViewHolder.loadData(turnUpBeans);
            }
        });
    }

    /**
     * 各分包商到场情况
     *
     * @param bean
     */
    private void updateTurnUp(PersonnelRecordBean bean) {
        if (bean != null) {
            pvJinkai.setMax(bean.getSumJinKai());
            pvJinkai.setProgress(bean.getJinKai());

            pvYinwai.setMax(bean.getSumYinWai());
            pvYinwai.setProgress(bean.getYinWai());

            pvEnter.setMax(bean.getSumR());
            pvEnter.setProgress(bean.getR());
        }
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
