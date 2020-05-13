package com.sir.app.wisdom.model;

import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.PersonnelContract;
import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.TurnUpBean;
import com.sir.library.retrofit.callback.RxSubscriber;
import com.sir.library.retrofit.exception.ResponseThrowable;
import com.sir.library.retrofit.transformer.ComposeTransformer;

import java.util.List;

/**
 * 人员管理模型
 * Created by zhuyinan on 2020/4/9.
 */
public class PersonnelModel extends Repository implements PersonnelContract {

    public static String EVENT_ADD_PERSONNEL = getEventKey();

    private MutableLiveData<List<PersonnelRecordBean>> personnelRecords;

    private MutableLiveData<List<TurnUpBean>> turnUpBean;

    @Override
    public void addPersonnel(String code, String nameCN, String nameEn, String photo) {
        String json = "{\"type\":\"add\",\"obj\":{\"Tid\":\"1\",\"StaffID\":\"0\",\"StaffCode\":\"%s\",\"CNFullName\":\"%s\",\"EN_FullName\":\"%s\",\"Photo\":\"%s\"}}";

        postState(ON_LOADING, "正在提交..");

        addSubscribe(appServerApi.addPersonnel(createBody(String.format(json, code, nameCN, nameEn, photo)))
                .compose(ComposeTransformer.<String>FlowableMsg())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postState(ON_SUCCESS, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, getLoginMsg(ex.code));
                    }
                }));
    }

    @Override
    public MutableLiveData<List<PersonnelRecordBean>> getPersonnelRecord() {
        if (personnelRecords == null) {
            personnelRecords = new MutableLiveData<>();
        }
        return personnelRecords;
    }

    @Override
    public MutableLiveData<List<TurnUpBean>> getTurnUp() {
        if (turnUpBean == null) {
            turnUpBean = new MutableLiveData<>();
        }
        return turnUpBean;
    }

    @Override
    public void personnelRecords() {
        addSubscribe(appServerApi.personnelRecords()
                .compose(ComposeTransformer.<List<PersonnelRecordBean>>Flowable())
                .subscribeWith(new RxSubscriber<List<PersonnelRecordBean>>() {
                    @Override
                    protected void onSuccess(List<PersonnelRecordBean> list) {
                        getPersonnelRecord().postValue(list);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, getLoginMsg(ex.code));
                    }
                }));
    }

    @Override
    public void getPerson() {
        addSubscribe(appServerApi.getTurnUp()
                .compose(ComposeTransformer.<List<TurnUpBean>>Flowable())
                .subscribeWith(new RxSubscriber<List<TurnUpBean>>() {
                    @Override
                    protected void onSuccess(List<TurnUpBean> bean) {
                        getTurnUp().postValue(bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, getLoginMsg(ex.code));
                    }
                }));
    }


    public String getLoginMsg(int code) {
        switch (code) {
            case 0:
                return "解析失敗";
            case 2:
                return "員工編號沒有填寫";
            case 3:
                return "姓名沒有填寫";
            case 4:
                return "登錄權限不足";
            case 5:
                return "照片解析失敗";
            case 6:
                return "員工編號已經存在";
            default:
                return "未知錯誤";
        }
    }
}
