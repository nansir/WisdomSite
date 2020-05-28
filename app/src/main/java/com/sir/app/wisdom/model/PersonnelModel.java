package com.sir.app.wisdom.model;

import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.PersonnelContract;
import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
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

    public static String EVENT_SEARCH_PERSONNEL = getEventKey();

    private MutableLiveData<List<PersonnelRecordBean>> personnelRecords;

    private MutableLiveData<List<TurnUpBean>> turnUpBean;

    private MutableLiveData<List<RecordPersonnelBean>> recordPersonnel;


    @Override
    public void addPersonnel(String code, String nameCN, String nameEn, String photo) {
        String json = "{\"type\":\"add\",\"obj\":{\"Tid\":\"1\",\"StaffID\":\"0\",\"StaffCode\":\"%s\",\"CNFullName\":\"%s\",\"EN_FullName\":\"%s\",\"Photo\":\"%s\",\"SourceType\":2}}";
        addSubscribe(appServerApi.addPersonnel(createBody(String.format(json, code, nameCN, nameEn, photo)))
                .compose(ComposeTransformer.<String>FlowableMsg())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postState(ON_SUCCESS, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, ex.message);
                    }
                }));
    }

    @Override
    public void editPersonnel(int id, String code, String nameCN, String nameEn, String photo) {
        String json = "{\"type\":\"edit\",\"obj\":{\"Tid\":\"1\",\"StaffID\":\"%s\",\"StaffCode\":\"%s\",\"CNFullName\":\"%s\",\"EN_FullName\":\"%s\",\"Photo\":\"%s\",\"SourceType\":2}}";
        addSubscribe(appServerApi.addPersonnel(createBody(String.format(json, id, code, nameCN, nameEn, photo)))
                .compose(ComposeTransformer.<String>FlowableMsg())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postState(ON_SUCCESS, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, ex.message);
                    }
                }));
    }

    @Override
    public void searchPersonnel(String staffCode) {
        String json = "{\"type\":\"search\",\"obj\":{\"Tid\":\"1\" ,\"StaffCode\":\"%s\"}}";
        addSubscribe(appServerApi.searchPersonnel(createBody(String.format(json, staffCode)))
                .compose(ComposeTransformer.<RecordPersonnelBean>Flowable())
                .subscribeWith(new RxSubscriber<RecordPersonnelBean>() {
                    @Override
                    protected void onSuccess(RecordPersonnelBean bean) {
                        postData(EVENT_SEARCH_PERSONNEL, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postData(EVENT_SEARCH_PERSONNEL, null);
                    }
                }));
    }

    @Override
    public void recordPersonnel(String key) {
        String json = "{\"type\":\"m_user_list\",\"obj\":{\"Tid\":\"1\",\"Key\":\"%s\" }}";
        addSubscribe(appServerApi.recordPersonnel(createBody(String.format(json, key)))
                .compose(ComposeTransformer.<List<RecordPersonnelBean>>Flowable())
                .subscribeWith(new RxSubscriber<List<RecordPersonnelBean>>() {
                    @Override
                    protected void onSuccess(List<RecordPersonnelBean> list) {
                        getRecordPersonnel().postValue(list);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, ex.message);
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
    public MutableLiveData<List<RecordPersonnelBean>> getRecordPersonnel() {
        if (recordPersonnel == null) {
            recordPersonnel = new MutableLiveData<>();
        }
        return recordPersonnel;
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
                        postState(ON_FAILURE, ex.message);
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
                        postState(ON_FAILURE, ex.message);
                    }
                }));
    }
}
