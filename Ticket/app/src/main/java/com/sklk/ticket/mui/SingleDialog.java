package com.sklk.ticket.mui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.sklk.ticket.R;
import com.sklk.ticket.listener.OnClickCommonDialogListener;


/**
 * Created by Administrator on 2017/11/14.
 */

public class SingleDialog extends Dialog {

    private final String mTitle;
    private final String mContent;
    private OnClickCommonDialogListener mOnClickCommonDialogListener;
    private TextView mConfirmTv;

    public SingleDialog(@NonNull Context context, String title, String content) {
        super(context, R.style.AlertDialogStyle);
        this.mTitle = title;
        this.mContent = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single);
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText(mTitle);
        TextView contentTv = (TextView) findViewById(R.id.content_tv);
        contentTv.setText(mContent);
        mConfirmTv = (TextView) findViewById(R.id.confirm_tv);
        mConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnClickCommonDialogListener) {
                    mOnClickCommonDialogListener.onConfirmListener();
                }
                dismiss();
            }
        });
    }


    public void setOnClickListenerWithCancelAndConfirm(OnClickCommonDialogListener onClickCommonDialogListener) {
        this.mOnClickCommonDialogListener = onClickCommonDialogListener;
    }

    public void setConfirmText(String confirmText) {
        mConfirmTv.setText(confirmText);
    }
}
