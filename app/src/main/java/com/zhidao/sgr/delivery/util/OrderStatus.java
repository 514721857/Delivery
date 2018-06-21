package com.zhidao.sgr.delivery.util;

/**
 * Created by 沈国荣 on 2018/6/21.
 * QQ:514721857
 * Description:
 */

public class OrderStatus {
//    0待付款；1已付款；2待配送；3派送中；4完成；-1已取消

    public static String getStatusName(int status){

        if(status==0){
            return "付款";
        }else if(status==1){
            return "完成";
        }else if(status==2){
            return "待配送";
        }else if(status==3){
            return "派送中";
        }else if(status==4){
            return "完成";
        }else{
            return "取消";
            }
    }


    public static String getStatusNameText(int status){

        if(status==0){
            return "待付款";
        }else if(status==1){
            return "已付款";
        }else if(status==2){
            return "待配送";
        }else if(status==3){
            return "派送中";
        }else if(status==4){
            return "完成";
        }else{
            return "待付款";
        }
    }

}
