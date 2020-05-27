package com.sir.app.wisdom.interceptor;

import android.os.Looper;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by zhuyinan on 2020/5/27.
 */
public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();
        Response response = chain.proceed(originalRequest);

        try {
            ResponseBody responseBody = response.body();
            //解决response.body().string();只能打印一次
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset UTF8 = Charset.forName("UTF-8");
            String string = buffer.clone().readString(UTF8);

            BaseResponseBean baseResponseBean = new Gson().fromJson(string, BaseResponseBean.class);

            if (baseResponseBean != null) {
                if (baseResponseBean.getCode().equals(ResponseCode.TOKEN_ERROR)) {
                    //token过期
                    //根据RefreshToken同步请求，获取最新的Token
                    String newToken = getNewToken();

                    //使用新的Token，创建新的请求
                    Request newRequest = chain.request()
                            .newBuilder()
                            .header("authToken", newToken)
                            .build();
                    //重新请求
                    return chain.proceed(newRequest);
                } else if (baseResponseBean.getCode().equals(ResponseCode.REFRESH_TOKEN_ERROR)) {
                    //refreshToken过期
                    EventBus.getDefault().post(new Handler(Looper.getMainLooper()).obtainMessage(Constants
                            .Key_EventBus_Msg.EVENT_TOKEN_OVERDUE));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 同步请求方式，根据RefreshToken获取最新的Token
     *
     * @return
     */
    private synchronized String getNewToken() throws IOException {
//        通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
        String newToken = "";
        String paramsRefresh = "";

        TokenInfoBean oldTokenBean = TokenInfoBean.readToken();
        if (oldTokenBean != null && !BaseUtils.isEmpty(oldTokenBean.getRefreshToken())) {
            paramsRefresh = oldTokenBean.getRefreshToken();
        }

        ApiService apiService = ApiRetrofit.create(ApiService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse(ContentTypeConstant.Content_TYPE_JSON), "");
        Call<BaseResponseBean<TokenInfoBean>> call = apiService.refreshToken(paramsRefresh, requestBody);
        BaseResponseBean<TokenInfoBean> responseBean = call.execute().body();
        if (responseBean != null && responseBean.getCode().equals(ResponseCode.RESPONSE_SUCCES) && responseBean.getData() != null && !BaseUtils.isEmpty(responseBean.getData().getToken())) {
            //获取新的token成功
            TokenInfoBean infoBean = responseBean.getData();
            TokenInfoBean.saveToken(infoBean);
            newToken = infoBean.getToken();
        }
        return newToken;
    }

}
