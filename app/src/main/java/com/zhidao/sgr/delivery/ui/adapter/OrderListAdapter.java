package com.zhidao.sgr.delivery.ui.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhidao.sgr.delivery.R;
import com.zhidao.sgr.delivery.model.OrderBean;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */




public class OrderListAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> implements BaseQuickAdapter.OnItemChildClickListener,BaseQuickAdapter.OnItemClickListener{
  Context context;
    public OrderListAdapter(Context contexts) {
        super(R.layout.layout_item_order, null);
        context=contexts;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OrderBean personItem) {

        baseViewHolder.setText(R.id.order_list_name,personItem.getUsername());

        baseViewHolder.setText(R.id.order_list_phone,personItem.getPhone());
        baseViewHolder.setText(R.id.order_list_address,personItem.getAddress());
        baseViewHolder.setText(R.id.order_list_list,personItem.getDetail());
        baseViewHolder.setText(R.id.order_list_song,"配送："+personItem.getExpressFee());
        baseViewHolder.setText(R.id.order_list_can,"  餐费"+personItem.getAmount());
        baseViewHolder.setText(R.id.order_list_total,"  合计"+personItem.getTotal());
        baseViewHolder.setText(R.id.order_list_zt,personItem.getStatus()+"状态");
        baseViewHolder.setText(R.id.order_list_time,new DateTime(personItem.getGmtCreate()).toDateTimeISO().toString());
       /* new DateTime(new Date()).toDateTime(DateTimeZone.UTC).toDateTimeString("yyyy年MM月dd日 hh时mm分ss秒");
        DateTime.ParseExact("2013-11-17T11:59:22+08:00","yyyy-MM-ddTHH:mm:ss+08:00",null)*/



    }




    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}