package com.zhidao.sgr.delivery.model;

/**
 * Data：2018/6/20/020-11:20
 * By  沈国荣
 * Description:
 */
public class RequestOrder {
    int status;
    int page;
    String address;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
