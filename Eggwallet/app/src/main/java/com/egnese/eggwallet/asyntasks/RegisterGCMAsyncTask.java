package com.egnese.eggwallet.asyntasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.egnese.eggwallet.GCMIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * Created by adityaagrawal on 23/11/15.
 */
public class RegisterGCMAsyncTask extends AsyncTask<Void, Void, Void>{

    private Context context;
    private Exception exceptionToBeCaught;

    public RegisterGCMAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            GCMRegistrar.checkDevice(context);
            GCMRegistrar.checkManifest(context);
            GCMRegistrar.register(context, GCMIntentService.SENDER_ID);
        }catch (Exception e){
            exceptionToBeCaught = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(exceptionToBeCaught != null){
            Log.d("log", exceptionToBeCaught.toString());
        }else{
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }
    }
}
