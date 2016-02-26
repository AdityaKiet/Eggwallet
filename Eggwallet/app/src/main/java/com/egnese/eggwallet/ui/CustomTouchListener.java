package com.egnese.eggwallet.ui;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by adityaagrawal on 21/12/15.
 */
public class CustomTouchListener implements View.OnTouchListener {
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                String string=((TextView)view).getText().toString();
                SpannableString content = new SpannableString(string);
                content.setSpan(new UnderlineSpan(), 0, string.length(), 0);
                ((TextView)view).setText(content);
                ((TextView)view).setTextColor(0xFF808080);
                 //white
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                String string1 =((TextView)view).getText().toString();
                SpannableString content1 = new SpannableString(string1);
                content1.setSpan(null, 0, string1.length(), 0);
                ((TextView)view).setText(content1);
                ((TextView)view).setTextColor(0xFF000000);
                 //black
                break;
        }
        return false;
    }
}