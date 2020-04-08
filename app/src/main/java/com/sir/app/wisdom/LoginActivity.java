package com.sir.app.wisdom;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.utils.AppUtils;
import com.sir.app.wisdom.view.MainPersonnelActivity;
import com.sir.app.wisdom.view.MainVehicleActivity;
import com.sir.library.com.AppBaseActivity;
import com.sir.library.com.AppLogger;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 登录界面
 * Created by zhuyinan on 2020/4/8.
 */
public class LoginActivity extends AppBaseActivity {

    final int RC_WRITE = 1001;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.et_login_name)
    EditText etLoginName;

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
        mViewHelper.setTextVal(R.id.et_login_name, "18575572721");
        mViewHelper.setTextVal(R.id.et_login_pwd, "18575572721");
        AppUtils.checkUpdate(this, RC_WRITE);
    }

    @Override
    protected boolean isUseFullScreenMode() {
        return true;
    }

    @OnClick({R.id.btn_login, R.id.tv_login_admin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginVerify(true);
                break;
            case R.id.tv_login_admin:
                loginVerify(false);
                break;
        }
    }

    /**
     * 登录账户验证
     */
    protected void loginVerify(boolean flag) {
        String account = etLoginName.getText().toString().trim();
        String password = etLoginPwd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            etLoginName.requestFocus();
            mDialog.showError("账户不能为空");
        } else if (TextUtils.isEmpty(password)) {
            etLoginPwd.requestFocus();
            mDialog.showError("密码不能为空");
        } else {
            // mViewModel.login(account, password);
            mDialog.showLoading("正在登录");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOperation.forward(flag ? MainPersonnelActivity.class : MainVehicleActivity.class);
                    finish();
                }
            }, 2000);
        }
    }


    @OnCheckedChanged({R.id.cb_pwd_show})
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        etLoginPwd.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        etLoginPwd.setSelection(etLoginPwd.getText().length());
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
