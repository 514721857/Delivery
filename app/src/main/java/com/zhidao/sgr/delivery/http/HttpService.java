package com.zhidao.sgr.delivery.http;


import com.zhidao.sgr.delivery.model.OrderBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Data：2018/1/23/023-16:01
 * By  沈国荣
 * Description: 接口类
 */

public interface HttpService {


    /**
     * 登录接口
     *
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("seller/order/list")
    Observable<ResponseBody> getSave();


    /**
     * 登录接口
     *
     * @param status
     * @return
     */
    @POST("seller/order/list")
    Observable<Result<List<OrderBean>>> getOrderList(@Query("status") String status);


}