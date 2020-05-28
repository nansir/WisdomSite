package com.sir.app.wisdom.interceptor;

import com.google.gson.Gson;
import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.common.AppServerApi;
import com.sir.app.wisdom.common.MyApplication;
import com.sir.app.wisdom.model.entity.LoginBean;
import com.sir.library.com.utils.SPUtils;
import com.sir.library.retrofit.HttpUtils;
import com.sir.library.retrofit.request.RetrofitClient;
import com.sir.library.retrofit.response.HttpResponse;

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
 * Token 过期拦截器
 * Created by zhuyinan on 2019/8/20.
 */
public class TokenInterceptor implements Interceptor {

    private Gson mGson = new Gson();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response response = chain.proceed(originalRequest);
        try {
            ResponseBody responseBody = response.body();
            //解决response.body().string();只能获取一次的。
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset UTF8 = Charset.forName("UTF-8");
            String json = buffer.clone().readString(UTF8);

            HttpResponse res = mGson.fromJson(json, HttpResponse.class);

            if (res.getResCode() == 401) {

                //根据RefreshToken同步请求，获取最新的Token
                LoginBean bean = getNewToken();

                //使用新的Token，创建新的请求
                Request newRequest = chain.request()
                        .newBuilder()
                        .header("Token", bean.getRefreshToken())
                        .header("Authorization", "Bearer " + bean.getAccessToken())
                        .build();

                //重新请求
                return chain.proceed(newRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    private LoginBean getNewToken() throws IOException {
        // 同步请求接口通过一个特定的接口获取新的token,此处要用到同步的retrofit请求

        String JSON = SPUtils.getInstance().get(AppKey.LOGIN);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON);

        RetrofitClient client = HttpUtils.getInstance(MyApplication.getContext()).getRetrofitClient();

        HttpResponse<LoginBean> response = client.builder(AppServerApi.class).token(body).execute().body();
        LoginBean bean = response.getResBody();

        HttpUtils.getInstance(MyApplication.getContext()).setAuthToken("Bearer " + bean.getAccessToken());
        HttpUtils.getInstance(MyApplication.getContext()).setToken(bean.getRefreshToken());

        SPUtils.getInstance().put(AppKey.AUTH_TOKEN, "Bearer " + bean.getAccessToken());
        SPUtils.getInstance().put(AppKey.TOKEN, bean.getRefreshToken());

        return bean;
    }
}
