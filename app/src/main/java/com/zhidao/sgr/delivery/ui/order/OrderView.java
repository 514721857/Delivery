package com.zhidao.sgr.delivery.ui.order;

import com.tz.mvp.framework.base.view.MvpView;
import com.zhidao.sgr.delivery.model.OrderBean;

import java.util.List;

/**
 * Data：2018/1/24/024-10:56
 * By  沈国荣
 * Description:V层接口
 */
public interface OrderView extends MvpView {
    void showResult(List<OrderBean> result);
    void showResultOnErr(String err);

}
