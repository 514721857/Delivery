package com.zhidao.sgr.delivery.ui.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.google.gson.Gson;

import com.zhangke.websocket.ErrorResponse;
import com.zhangke.websocket.Response;
import com.zhidao.sgr.delivery.R;
import com.zhidao.sgr.delivery.config.AppCon;
import com.zhidao.sgr.delivery.config.MvpWebSocketActivity;
import com.zhidao.sgr.delivery.model.OrderBean;
import com.zhidao.sgr.delivery.ui.LoginActivity;
import com.zhidao.sgr.delivery.ui.adapter.OrderListAdapter;
import com.zhidao.sgr.delivery.util.OrderStatus;
import com.zhidao.sgr.delivery.util.SoundPoolPlayer;
import com.zhidao.sgr.delivery.util.StartActivityUtil;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class OrderActivity extends MvpWebSocketActivity<OrderView,OrderPresenter> implements OrderView ,OnItemChildClickListener{

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;


    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OrderListAdapter mAdapter;

    private int mNextRequestPage = 0;
    private static final int PAGE_SIZE = 10;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    String address;
    private int status=3;//默认是待配送



    @BindView(R.id.order_text_dzz)
    TextView order_text_dzz;

    @BindView(R.id.order_text_dps)
    TextView order_text_dps;

    @BindView(R.id.order_text_zzz)
    TextView order_text_zzz;

    @BindView(R.id.top_view_left)
    TextView top_view_left;

    Badge wm_badeg_text;
    SoundPoolPlayer mPlayer;
    String userID;

    //设置默认
    private void setMoren(){

        setWmChoice(0);
        wm_badeg_text=new QBadgeView(this).bindTarget(order_text_dzz).setBadgeText("").setBadgeGravity(Gravity.END | Gravity.TOP);
        mPlayer = SoundPoolPlayer.create(this, R.raw.tip);
        mPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) { 	//mp will be null here
                        Log.d("debug", "completed");
                    }
                }
        );

    }








    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }
    @OnClick({R.id.top_view_right_text,R.id.layout_zzz,R.id.layout_dzz,R.id.layout_dps
            ,R.id.top_view_left})
    public void onClick(View view) {
        switch (view.getId()) {
            /////////////////////////////////////////////外卖
            case R.id.layout_dps:// 已完成
                status=5;
                refresh();
                setWmChoice(2);

                break;
            case R.id.layout_zzz:// 配送中
                status=4;
                refresh();
                setWmChoice(1);
                break;
            case R.id.layout_dzz:// 待配送
           /*     if(!mPlayer.isPlaying()){
                    mPlayer.play();
                }*/

                status=3;
                refresh();
                wm_badeg_text.hide(true);
                setWmChoice(0);
                break;
            case R.id.top_view_right_text:

                editor = pref.edit();
                editor.putString(AppCon.SCCESS_TOKEN_KEY,"");
                editor.commit();
                StartActivityUtil.skipAnotherActivity(this, LoginActivity.class);
                break;



        }
    }


    private void setWmChoice(int i){
        clearWm();
        if(i==0){
            order_text_dzz.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==1){
            order_text_zzz.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==2){
            order_text_dps.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }
    }
    private void clearWm(){
        order_text_dzz.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        order_text_dps.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        order_text_zzz.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
    }



    @Override
    protected void initView() {
        super.initView();
        pref = this.getSharedPreferences(AppCon.USER_KEY,MODE_PRIVATE);
        userID= pref.getString(AppCon.USER_USER_ID,"");
        setMoren();
        initMenu();
//        getPresenter().getAddress1();
        //init city menu

    }


    private void initMenu(){
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
        getPresenter().getOrderList(status,mNextRequestPage);
    }

    private void refresh() {
        mNextRequestPage = 0;
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        getPresenter().getOrderList(status,mNextRequestPage);
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
        if(result!=null){
            System.out.println("显示数据"+result.size());
        }else{
            System.out.println("没有数据");
        }

        if(mNextRequestPage==0){
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


    @SuppressLint("MissingPermission")
    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view,final int position) {
        switch (view.getId()) {


            case R.id.order_list_zt://修改订单状态
                OrderBean updateOrder=  (OrderBean) adapter.getData().get(position);
                if(updateOrder.getStatus()==3){//需要设置时间//按下开始制作
                    updateOrder.setPsId(userID);
                    updateOrder.setStatus(4);
                }else if(updateOrder.getStatus()==4){//按下完成制作

                    updateOrder.setStatus(5);
                }else {
                    return;
                }
                getPresenter().UpdateOrder(updateOrder,position);

                break;

            case R.id.order_btn_phone://打电话
                OrderBean temp=  (OrderBean) adapter.getData().get(position);
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + temp.getPhone());
                intent.setData(data);
                OrderActivity.this.startActivity(intent);
                break;
     /*       case R.id.order_btn_dy://打印



                break;*/

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onMessageResponse(Response message) {
        System.out.println("新消息提示"+message.getResponseText());

    }

    @Override
    public void onSendMessageError(ErrorResponse error) {
        System.out.println("新消息错误"+error.getResponseText());
    }
}
