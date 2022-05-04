package com.example.cardviewdemo.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.cardviewdemo.Chat.MessagingActivity;


public class BackKeyEditText extends AppCompatEditText {

    Context mContext;

    public BackKeyEditText(Context context) {
        super(context);
        mContext = context;
    }

    public BackKeyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {

           /* if (mContext instanceof UserChatActivity) {
                ((UserChatActivity) mContext).onBackKeyPressedOnKeyboard();
            }*/

            if (mContext instanceof MessagingActivity) {
                ((MessagingActivity) mContext).onBackKeyPressedOnKeyboard();
            }

            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}