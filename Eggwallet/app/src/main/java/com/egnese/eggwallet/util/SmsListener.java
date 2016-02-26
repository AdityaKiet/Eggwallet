package com.egnese.eggwallet.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import com.egnese.eggwallet.constants.EggWallet;

/**
 * Created by adityaagrawal on 25/11/15.
 */
public class SmsListener extends BroadcastReceiver implements EggWallet{

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        if(msg_from.equals(SENDER_ID)){
                            Intent intent1 = new Intent("otp-received");
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("message", msgBody);
                            intent1.putExtras(bundle1);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                        }
                    }
                }catch(Exception e){
                }
            }
        }
    }
}