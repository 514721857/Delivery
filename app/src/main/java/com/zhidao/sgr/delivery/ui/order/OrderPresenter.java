package com.zhidao.sgr.delivery.ui.order;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.zhidao.sgr.delivery.config.BasePresenter;
import com.zhidao.sgr.delivery.http.HttpUtils;
import com.zhidao.sgr.delivery.model.CommonModel;
import com.zhidao.sgr.delivery.model.OrderBean;
import com.zhidao.sgr.delivery.model.OrderRespons;
import com.zhidao.sgr.delivery.model.Result;
import com.zhidao.sgr.delivery.ui.order.OrderView;

import java.util.List;


/**
 * Data：2018/1/24/024-10:58
 * By  沈国荣
 * Description:
 */
public class OrderPresenter extends BasePresenter<OrderView> {
    private CommonModel commonModel;
    private Context contexts;
    private SharedPreferences sp ;
    public OrderPresenter(Context context) {
        super(context);
        this.contexts=context;
        this.commonModel = new CommonModel(context);
    }
    public void getOrderList(int status,int page){
        commonModel.getOrderList(status, page, new HttpUtils.OnHttpResultListener() {
            @Override
            public void onResult(Object result) {
                Result<OrderRespons> temp=(Result<OrderRespons>)result;
                System.out.println("显示条数"+temp.content.getTotalElements());
                if(temp.status.equals("200")){
                    getView().showResult(temp.content.getData());
                }else{
                    getView().showResultOnErr(temp.message);

                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void UpdateOrder(OrderBean order, final int position){
        commonModel.UpdateOrder(order, new HttpUtils.OnHttpResultListener() {
            @Override
            public void onResult(Object result) {
                Result<OrderRespons> temp=(Result<OrderRespons>)result;
                if(temp.status.equals("200")){
                    getView().UpdateSussess(position);
                }else{
                    getView().showResultOnErr(temp.message);

                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }





}
