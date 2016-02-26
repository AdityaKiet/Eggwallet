package com.egnese.eggwallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.egnese.eggwallet.activities.AppInfoActivity;
import com.egnese.eggwallet.dto.SessionDTO;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = "EggWallet::Service";
	public static final String SENDER_ID = "339249554709";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		SessionDTO sessionDTO  = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
		if(sessionDTO.getGcmID() == null){
            sessionDTO.setGcmID(registrationId);
            editor.putString("session", gson.toJson(sessionDTO));
            editor.commit();
            Intent intent = new Intent(this, AppInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{

		}

		Log.i(TAG, "onRegistered: registrationId=" + registrationId);


	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent data){
        String message;
        message = data.getStringExtra("message");
        Log.i(TAG, "onSucyess: suc=" + message);
    }

	@Override
	protected void onError(Context arg0, String errorId) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        SessionDTO sessionDTO  = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        if(sessionDTO.getGcmID() == null){
            Intent intent = new Intent("gcm-registration");
            intent.putExtra("message", errorId);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        }

		Log.i(TAG, "onError: errorId=" + errorId);
	}

}