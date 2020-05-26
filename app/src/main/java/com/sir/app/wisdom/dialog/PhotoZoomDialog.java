package com.sir.app.wisdom.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.view.weight.PhotoZoomView;
import com.sir.library.base.BaseDialog;

/**
 * Created by zhuyinan on 2020/5/5.
 */
public class PhotoZoomDialog extends BaseDialog {

    public PhotoZoomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.dialog_photo_zoom;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //设置window背景，默认的背景会有Padding值。
        getWindow().setBackgroundDrawableResource(R.mipmap.ic_bg);
        //在setContentView之后调用
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void doBusiness() {

    }

    public void setImage(String uri) {
        PhotoZoomView imageView = findViewById(R.id.iv_photo_preview);
        imageView.enable();
        Glide.with(getContext())
                .load(uri)
                .thumbnail(0.1f)
                .dontAnimate()
                .dontTransform()
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_placeholder)
                .into(imageView);

        mViewHelper.setOnClickListener(R.id.ibtn_close, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoZoomDialog.this.dismiss();
            }
        });
    }
}
