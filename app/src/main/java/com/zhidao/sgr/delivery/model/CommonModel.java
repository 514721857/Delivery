package com.zhidao.sgr.delivery.model;

import android.content.Context;

import com.zhidao.sgr.delivery.http.HttpService;
import com.zhidao.sgr.delivery.http.HttpUtils;

import io.reactivex.Observable;


/**
 * 作者: Dream on 16/9/24 21:09
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class CommonModel extends BaseModel {

    public CommonModel(Context context) {
        super(context);
    }

    /**
     * 获取二维码
     *
     * @param onLceHttpResultListener
     */
    public void getSave(final HttpUtils.OnHttpResultListener onLceHttpResultListener) {

        HttpService essenceService= buildService(HttpService.class);

        buildObserve((Observable)essenceService.getSave(),onLceHttpResultListener);

    }
}
