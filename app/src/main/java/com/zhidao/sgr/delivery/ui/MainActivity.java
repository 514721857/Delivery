package com.zhidao.sgr.delivery.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhidao.sgr.delivery.R;
import com.zhidao.sgr.delivery.http.HttpUtils;
import com.zhidao.sgr.delivery.model.CommonModel;
import com.zhidao.sgr.delivery.model.OrderBean;
import com.zhidao.sgr.delivery.model.Result;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    CommonModel commonModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commonModel=new CommonModel(this);
        result= (TextView) this.findViewById(R.id.result);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonModel.getSave(new HttpUtils.OnHttpResultListener() {
                    @Override
                    public void onResult(Object result) {

                        ResponseBody tempBody = (ResponseBody)result;


                        String tempResult= null;
                        try {
                            tempResult = tempBody.string().toString();
                            Toast.makeText(MainActivity.this,tempResult,Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        /*Result<List<OrderBean>> temp=(Result<List<OrderBean>>)result;
                        List<OrderBean>   tempResult=   ((Result<List<OrderBean>>) result).content;*/

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        });
    }
}
