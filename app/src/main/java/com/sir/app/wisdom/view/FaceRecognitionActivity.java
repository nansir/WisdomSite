package com.sir.app.wisdom.view;

import android.view.Menu;
import android.view.MenuItem;

import com.sir.app.wisdom.R;
import com.sir.library.com.AppBaseActivity;
import com.sir.library.com.AppLogger;

/**
 * 人脸识别活动
 * Created by zhuyinan on 2020/4/8.
 */
public class FaceRecognitionActivity extends AppBaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_face_recognition;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_camera) {
            AppLogger.toast("切換");
        }
        return super.onOptionsItemSelected(item);
    }
}
