package com.zhidao.sgr.delivery.ui.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.google.gson.Gson;
import com.zhidao.sgr.delivery.R;
import com.zhidao.sgr.delivery.config.AppCon;
import com.zhidao.sgr.delivery.config.BaseMvpActivity;
import com.zhidao.sgr.delivery.http.HttpUtils;
import com.zhidao.sgr.delivery.model.CommonModel;
import com.zhidao.sgr.delivery.model.OrderBean;
import com.zhidao.sgr.delivery.model.Result;
import com.zhidao.sgr.delivery.model.User;
import com.zhidao.sgr.delivery.ui.LoginActivity;
import com.zhidao.sgr.delivery.ui.adapter.OrderListAdapter;
import com.zhidao.sgr.delivery.util.StartActivityUtil;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class OrderActivity extends BaseMvpActivity<OrderView,OrderPresenter> implements OrderView ,OnItemChildClickListener{
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OrderListAdapter mAdapter;

    private int mNextRequestPage = 1;
    private static final int PAGE_SIZE = 10;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    String address;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }
    @OnClick({R.id.result,R.id.top_view_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.result:


                new CommonModel(this).getSave(new HttpUtils.OnHttpResultListener() {
                    @Override
                    public void onResult(Object result) {
                        ResponseBody tempBody = (ResponseBody)result;
                        String tempResult= null;
                        try {
                            tempResult = tempBody.string().toString();
                            Toast.makeText(OrderActivity.this,tempResult,Toast.LENGTH_SHORT).show();
                            Log.d("tempResult",tempResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
                break;
                case R.id.top_view_right_text:
                    pref = this.getSharedPreferences(AppCon.USER_KEY,MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString(AppCon.SCCESS_TOKEN_KEY,"");
                    editor.commit();
                    StartActivityUtil.skipAnotherActivity(this, LoginActivity.class);
                    break;

        }
    }

    @Override
    protected void initView() {
        super.initView();
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter = new OrderListAdapter(this);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        refresh();
    }


    private void loadMore() {
        getPresenter().getOrderList(1,mNextRequestPage,address);
    }

    private void refresh() {
        mNextRequestPage = 1;
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
       getPresenter().getOrderList(1,mNextRequestPage,address);
    }


    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
//            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public OrderPresenter createPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    public void UpdateSussess(int position) {
        mAdapter.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showResult(List<OrderBean> result) {
        if(mNextRequestPage==1){
            setData(true,result);
            mAdapter.setEnableLoadMore(true);
            mSwipeRefreshLayout.setRefreshing(false);
        }else{
            setData(false, result);
        }


    }

    @Override
    public void showResultOnErr(String err) {
               Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {

            case R.id.order_btn_phone://打电话
                OrderBean temp=  (OrderBean) adapter.getData().get(position);
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + temp.getPhone());
                intent.setData(data);
                OrderActivity.this.startActivity(intent);
                break;
            case R.id.order_list_zt:
                OrderBean temp1=  (OrderBean) adapter.getData().get(position);
                OrderBean orderBean=new OrderBean();
                orderBean.setStatus(4);
                orderBean.setPhone(temp1.getPhone());
                orderBean.setUsername(temp1.getUsername());
                orderBean.setSummary(temp1.getSummary());
                orderBean.setAddress(temp1.getAddress());
                orderBean.setExpressFee(temp1.getExpressFee());
                orderBean.setAmount(temp1.getAmount());
                orderBean.setTotal(temp1.getTotal());
                orderBean.setDetail(temp1.getDetail());

                getPresenter().UpdateOrder(orderBean,position);
                break;

        }
    }
}
