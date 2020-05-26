package com.sir.app.wisdom.glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by zhuyinan on 2020/2/21.
 */
public class GlideCut {

    /**
     * 圆形
     *
     * @return
     */
    public static RequestOptions ROUND() {
        return RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)//不做磁盘缓存
                .skipMemoryCache(true);                 //不做内存缓存
    }

    /**
     * 圆角
     *
     * @return
     */
    public static RequestOptions FILLET() {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        return RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
    }
}
