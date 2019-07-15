package com.sklk.ticket.base.mvp;


import com.sklk.ticket.R;
import com.sklk.ticket.utils.ToastCommon;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {
    protected V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public boolean assertionObjIsNotNull(Object obj) {
        if (null == mView) {
            return false;
        }
        if (null == obj) {
            ToastCommon.nativityToast(mView.getContext(), R.string.nodata_obtained);
            return false;
        }
        return true;
    }
}
