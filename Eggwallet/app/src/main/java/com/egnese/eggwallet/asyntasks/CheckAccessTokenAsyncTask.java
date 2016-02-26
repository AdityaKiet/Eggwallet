package com.egnese.eggwallet.asyntasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.egnese.eggwallet.activities.DashboardActivity;
import com.egnese.eggwallet.activities.GCMActivity;
import com.egnese.eggwallet.activities.LoginActivity;
import com.egnese.eggwallet.constants.NetworkContsants;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.dto.SessionDTO;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by adityaagrawal on 02/12/15.
 */
public class CheckAccessTokenAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkContsants {
    private Context context;
    private UserxDTO userxDTO;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "";
    private int statusCode = 0;

    public CheckAccessTokenAsyncTask(Context context, UserxDTO userxDTO) {
        this.context = context;
        this.userxDTO = userxDTO;
    }



    @Override
    protected Void doInBackground(Void... params) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        list.add(new BasicNameValuePair("mobile", userxDTO.getMobile()));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + CHECK_ACCESS_TOKEN);

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            is = entity.getContent();
            statusCode = httpResponse.getStatusLine().getStatusCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line);
            is.close();
            result = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionToBeThrown = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {

            if (exceptionToBeThrown != null) {
                tryAgain();
            } else {
                if (statusCode >= 200 && statusCode <= 299) {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonObject = new JSONObject(jsonObject.getString("response"));
                    jsonObject = new JSONObject(jsonObject.get("data").toString());
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    SessionDTO sessionDTO = gson.fromJson(sharedPreferences.getString("session", null) , SessionDTO.class);
                    userxDTO = sessionDTO.getUserxDTO();
                    userxDTO.setAccessToken(jsonObject.getString("accessToken"));
                    sessionDTO.setUserxDTO(userxDTO);
                    editor.putString("session", gson.toJson(sessionDTO));
                    editor.commit();

                    Intent intent = new Intent(context, DashboardActivity.class);
                    ((ActionBarActivity)context).finish();
                    context.startActivity(intent);

                } else {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonObject = new JSONObject(jsonObject.getString("error"));
                    if(jsonObject.getString("name").equals("InvalidData")){

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        Gson gson = new Gson();
                        SessionDTO sessionDTO = gson.fromJson(sharedPreferences.getString("session", null) , SessionDTO.class);
                        sessionDTO.setUserxDTO(null);
                        editor.putString("session", gson.toJson(sessionDTO));
                        editor.putBoolean("isLogin", false);
                        editor.commit();

                        Intent intent = new Intent(context, LoginActivity.class);
                        ((ActionBarActivity)context).finish();
                        context.startActivity(intent);

                    }else{
                        tryAgain();
                    }
                }
            }
        }catch (Exception e){
            tryAgain();
        }
    }


    private void tryAgain(){
        if(GCMActivity.active) {
            final MaterialDialog mMaterialDialog = new MaterialDialog(context);
            mMaterialDialog.setTitle("Network Error");
            mMaterialDialog.setMessage("Could not communicate with servers.")
                    .setPositiveButton("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckAccessTokenAsyncTask checkAccessTokenAsyncTask = new CheckAccessTokenAsyncTask(context, userxDTO);
                            checkAccessTokenAsyncTask.execute();
                        }
                    })
                    .setNegativeButton("CANCEL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            ((ActionBarActivity) context).finish();
                        }
                    });
            mMaterialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ((ActionBarActivity) context).finish();
                }
            });
            mMaterialDialog.show();
        }
    }
}