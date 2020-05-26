package com.sir.app.wisdom.view;

import androidx.lifecycle.Observer;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.adapter.PersonnelAdapter;
import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
import com.sir.app.wisdom.vm.PersonnelViewModel;
import com.sir.library.mvvm.AppHolderActivity;
import com.sir.library.refresh.OnRefreshListener;
import com.sir.library.refresh.holder.RecyclerSwipeViewHolder;

import java.util.List;

import butterknife.OnClick;

/**
 * 人员信息上传记录
 * Created by zhuyinan on 2020/4/8.
 */
public class PersonnelRecordActivity extends AppHolderActivity<PersonnelViewModel, RecyclerSwipeViewHolder> implements OnRefreshListener {

    @Override
    public int bindLayout() {
        return R.layout.activity_personnel_record;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
        mViewHolder.setAdapter(new PersonnelAdapter(getActivity()));
        mViewHolder.setOnRefreshListener(this);
        mViewHolder.setEmptyMsg("暫無人員記錄");
        mViewModel.recordPersonnel("");
    }

    @Override
    protected void dataObserver() {
        mViewModel.getRecordPersonnel().observe(this, new Observer<List<RecordPersonnelBean>>() {
            @Override
            public void onChanged(List<RecordPersonnelBean> list) {
                mViewHolder.adapter.clearAllItem();
                mViewHolder.loadData(list);
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        String key = mViewHelper.getEditVal(R.id.et_search);
        mViewModel.recordPersonnel(key);
    }

    @Override
    public void onRefresh() {
        mViewModel.recordPersonnel("");
    }
}
