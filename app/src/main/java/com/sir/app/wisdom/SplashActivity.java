package com.sir.app.wisdom;

import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;

import com.sir.app.wisdom.utils.AppUtils;
import com.sir.app.wisdom.view.MainPersonnelActivity;
import com.sir.library.com.AppBaseActivity;

/**
 * 启动页
 * Created by zhuyinan on 2020/1/13.
 */
public class SplashActivity extends AppBaseActivity {

    private Handler handler = new Handler(msg -> {
        mOperation.forward(MainPersonnelActivity.class);
        finish();
        return false;
    });

    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void doBusiness() {
        TextView tvTitle = findViewById(R.id.tv_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/庞门正道标题体2.0增强版.ttf");
        tvTitle.setTypeface(typeface); //设置第三方字体
        mViewHelper.setTextVal(R.id.tv_version_number, AppUtils.getLocalVersionName(this));
        handler.sendEmptyMessageDelayed(0, 1500);
    }
}
