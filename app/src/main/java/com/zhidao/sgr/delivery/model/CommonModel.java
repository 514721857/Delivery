package com.zhidao.sgr.delivery.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zhidao.sgr.delivery.config.AppCon;
import com.zhidao.sgr.delivery.http.HttpService;
import com.zhidao.sgr.delivery.http.HttpUtils;

import io.reactivex.Observable;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;


/**
 * 作者: Dream on 16/9/24 21:09
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class CommonModel extends BaseModel {

    String token;
    SharedPreferences pref ;
    public CommonModel(Context context) {
        super(context);
        pref = context.getSharedPreferences(AppCon.USER_KEY,MODE_PRIVATE);
        token= pref.getString(AppCon.SCCESS_TOKEN_KEY,"");
    }

    /**
     * 测试
     *
     * @param onLceHttpResultListener
     */
    public void getSave(final HttpUtils.OnHttpResultListener onLceHttpResultListener) {

        RequestOrder order=new RequestOrder();
        order.setStatus(1);
        Gson gson=new Gson();
        String obj=gson.toJson(order);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);
        HttpService essenceService= buildService(HttpService.class);
        buildObserve((Observable)essenceService.getSave(token,body),onLceHttpResultListener);

    }

    /**
     * 登录
     *
     * @param onLceHttpResultListener
     */
    public void getLogin(User info, final HttpUtils.OnHttpResultListener onLceHttpResultListener) {
        Gson gson=new Gson();
        String obj=gson.toJson(info);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);
        HttpService essenceService= buildService(HttpService.class);
        buildObserve((Observable)essenceService.getLogin(body),onLceHttpResultListener);

    }

    /**
     * 登录
     *
     * @param onLceHttpResultListener
     */
    public void getOrderList(int  status,int page, final HttpUtils.OnHttpResultListener onLceHttpResultListener) {
       RequestOrder order=new RequestOrder();
        order.setStatus(status);
        Gson gson=new Gson();
        String obj=gson.toJson(order);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);
        HttpService essenceService= buildService(HttpService.class);
        buildObserve((Observable)essenceService.getOrderList(token,body),onLceHttpResultListener);

    }

}
