package com.zhidao.sgr.delivery.http;


import com.zhidao.sgr.delivery.model.OrderBean;
import com.zhidao.sgr.delivery.model.Result;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    @POST("seller/login")
    Observable<Result<String>> getLogin(@Body RequestBody info);


    /**
     * 测试接口
     *
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("seller/order/list")
    Observable<ResponseBody> getSave(@Header("token") String token, @Body RequestBody info);


    /**
     * 获得订单接口
     *@param info 请求体
     * @param token 请求头
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("seller/order/list")
    Observable<Result<List<OrderBean>>> getOrderList(@Header("token") String token, @Body RequestBody info);

}