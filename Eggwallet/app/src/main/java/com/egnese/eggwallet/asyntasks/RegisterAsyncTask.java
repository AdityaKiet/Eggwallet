package com.egnese.eggwallet.asyntasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.activities.ConfirmOTPActivity;
import com.egnese.eggwallet.constants.EggWallet;
import com.egnese.eggwallet.constants.NetworkContsants;
import com.egnese.eggwallet.dto.ErrorDTO;
import com.egnese.eggwallet.dto.MessageCustomDialogDTO;
import com.egnese.eggwallet.dto.SessionDTO;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.ui.MessageDialog;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public class RegisterAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkContsants, EggWallet{
    private Context context;
    private SweetAlertDialog pDialog;
    private UserxDTO userxDTO;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "", deviceId, country;
    private int statusCode = 0;
    private SessionDTO sessionDTO;

    public RegisterAsyncTask(Context context, UserxDTO userxDTO, String deviceId, String country){
        this.context = context;
        this.userxDTO = userxDTO;
        this.deviceId = deviceId;
        this.country = country;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
    }

    @Override
    protected void onPreExecute() {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.primary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        list.add(new BasicNameValuePair("referredCode", userxDTO.getReferralCode()));
        list.add(new BasicNameValuePair("mobile", userxDTO.getMobile()));
        list.add(new BasicNameValuePair("gcmKey", sessionDTO.getGcmID()));
        list.add(new BasicNameValuePair("realm", REALM));
        list.add(new BasicNameValuePair("locale", country));
        list.add(new BasicNameValuePair("deviceId", deviceId));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + REGISTER_URL);

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
        pDialog.dismissWithAnimation();

        try {
            if (exceptionToBeThrown != null) {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(exceptionToBeThrown.getMessage());
                messageCustomDialogDTO.setContext(context);
                MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                messageCustomDialog.error();

            } else {
                JSONObject jsonObject = new JSONObject(result);
                if (statusCode >= 200 && statusCode <= 299) {
                    if(jsonObject.has("response")) {
                        userxDTO = new Gson().fromJson(jsonObject.getString("response"), UserxDTO.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userxDTO", new Gson().toJson(userxDTO));
                        bundle.putString("country", country);
                        Intent intent = new Intent(context, ConfirmOTPActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }else if(jsonObject.has("error")){
                        ErrorDTO errorDTO = new Gson().fromJson(jsonObject.getString("error"), ErrorDTO.class);

                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setTitle(errorDTO.getName());
                        messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(errorDTO.getMessage());
                        messageCustomDialogDTO.setContext(context);
                        MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                        messageCustomDialog.error();
                    }else{
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                        messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
                        messageCustomDialogDTO.setContext(context);
                        MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                        messageCustomDialog.error();
                    }
                } else {
                    ErrorDTO errorDTO = new Gson().fromJson(jsonObject.getString("error"), ErrorDTO.class);

                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(errorDTO.getName());
                    messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(errorDTO.getMessage());
                    messageCustomDialogDTO.setContext(context);
                    MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                    messageCustomDialog.error();
                }
            }
        }catch (Exception e){
            MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
            messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
            messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
            messageCustomDialogDTO.setMessage(e.getMessage());
            messageCustomDialogDTO.setContext(context);
            MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
            messageCustomDialog.error();
        }
    }
}
