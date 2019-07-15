package com.sklk.ticket.network;

import android.content.Context;

import com.sklk.ticket.R;
import com.sklk.ticket.utils.NetWorkUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：${JHuiQ} on 2018/4/9 13:57
 */
public class ExecuteHttpManger {

    public static <T> void executeHttp(final Context context, Observable<T> observable, final NetworkCallback<T> callback) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (null == callback) {
                            return;
                        }
                        callback.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        if (null == callback) {
                            return;
                        }
                        callback.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null == callback) {
                            return;
                        }
                        //调用了onError就不会调用onCompleted
                        callback.onCompleted();
                        if (!NetWorkUtil.hasNetWork()) {
                            callback.onError(context.getString(R.string.network_unavailable));
                            return;
                        }
                        if (e.toString().contains(context.getString(R.string.timeout_exception))) {
                            callback.onError(context.getString(R.string.network_timeout));
                            return;
                        }
                        if (e.toString().contains(context.getString(R.string.eof_exception))) {
                            callback.onError(context.getString(R.string.parsing_failure));
                            return;
                        }
                        if (e.toString().contains(context.getString(R.string.connect_exception))) {
                            callback.onError(context.getString(R.string.connection_to_server_failed));
                            return;
                        }
                        callback.onError(context.getString(R.string.failed_to_get_data));
                    }

                    @Override
                    public void onNext(T t) {
                        if (null == callback) {
                            return;
                        }
                        if (null == t) {
                            callback.onError(context.getString(R.string.failed_to_get_data));
                            return;
                        }
                        callback.onNext(t);
                    }
                });

    }

}
