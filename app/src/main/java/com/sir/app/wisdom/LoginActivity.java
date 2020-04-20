package com.sir.app.wisdom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.sir.app.wisdom.common.AppConstant;
import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.model.AccountModel;
import com.sir.app.wisdom.model.entity.LoginBean;
import com.sir.app.wisdom.utils.AppUtils;
import com.sir.app.wisdom.view.MainPersonnelActivity;
import com.sir.app.wisdom.view.MainVehicleActivity;
import com.sir.app.wisdom.vm.AccountViewModel;
import com.sir.library.com.AppLogger;
import com.sir.library.com.utils.SPUtils;
import com.sir.library.mvvm.AppActivity;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 登录界面
 * Created by zhuyinan on 2020/4/8.
 */
public class LoginActivity extends AppActivity<AccountViewModel> {

    final int RC_WRITE = 1001;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.et_login_name)
    EditText etLoginName;
    @BindView(R.id.cb_login_remember)
    CheckBox cbLoginRemember;
    boolean flag;
    private Handler mHandler = new Handler(msg -> {
        if (1 == msg.what) {
            finish();
        }
        return true;
    });

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void doBusiness() {
        mViewHelper.setTextVal(R.id.et_login_name, SPUtils.getInstance().get(AppKey.ACCOUNT));
        mViewHelper.setTextVal(R.id.et_login_pwd, SPUtils.getInstance().get(AppKey.PASSWORD));
        cbLoginRemember.setChecked(SPUtils.getInstance().get(AppKey.REMEMBER, false));
        AppUtils.checkUpdate(this, RC_WRITE);
    }

    @Override
    protected void dataObserver() {
        mViewModel.subscribe(AccountModel.EVENT_LOGIN, LoginBean.class)
                .observe(this, new Observer<LoginBean>() {
                    @Override
                    public void onChanged(LoginBean bean) {
                        String account = etLoginName.getText().toString().trim();
                        String password = etLoginPwd.getText().toString().trim();
                        mViewModel.loginConfig(account, password, cbLoginRemember.isChecked());
                        mOperation.forward(flag ? MainPersonnelActivity.class : MainVehicleActivity.class);
                        finish();
                    }
                });

    }

    @OnClick({R.id.btn_login, R.id.tv_login_admin, R.id.iv_logo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginVerify(true);
                break;
            case R.id.tv_login_admin:
                loginVerify(false);
                break;
            case R.id.iv_logo:
                showConfigInput();
                break;
        }
    }

    /**
     * 登录账户验证
     */
    protected void loginVerify(boolean flag) {
        this.flag = flag;
        String account = etLoginName.getText().toString().trim();
        String password = etLoginPwd.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            etLoginName.requestFocus();
            mDialog.showError("账户不能为空");
        } else if (TextUtils.isEmpty(password)) {
            etLoginPwd.requestFocus();
            mDialog.showError("密码不能为空");
        } else {
            mViewModel.singIn(account, password);
        }
    }


    /**
     * 显示配置输入
     */
    private void showConfigInput() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.view_config_value, null);

        EditText address = view.findViewById(R.id.et_service_address);
        address.setText(SPUtils.getInstance().get(AppKey.IP, AppConstant.HTTP));

        address.setSelection(address.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view).setTitle("服务器配置").setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("保存", (dialog, i) -> {
                    String ip = address.getText().toString();
                    SPUtils.getInstance().put(AppKey.IP, ip);
                });

        builder.create().show();
    }


    @OnCheckedChanged({R.id.cb_pwd_show})
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        etLoginPwd.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        etLoginPwd.setSelection(etLoginPwd.getText().length());
    }

    @Override
    protected boolean isUseFullScreenMode() {
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_WRITE:
                //grantResults数组存储的申请的返回结果，
                //PERMISSION_GRANTED 表示申请成功
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权成功，
                    AppUtils.checkUpdate(getActivity(), RC_WRITE);
                } else {
                    //授权失败，简单提示用户
                    AppLogger.toast("授权失败,无法使用");
                    mHandler.sendEmptyMessageDelayed(1, 3000);
                }
                break;
            default:
                break;
        }
    }
}
