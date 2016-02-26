package com.egnese.eggwallet.asyntasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.activities.ResetPasswordActivity;
import com.egnese.eggwallet.constants.EggWallet;
import com.egnese.eggwallet.constants.NetworkContsants;
import com.egnese.eggwallet.dto.ErrorDTO;
import com.egnese.eggwallet.dto.MessageCustomDialogDTO;
import com.egnese.eggwallet.dto.RequestDTO;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.ui.MessageDialog;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by adityaagrawal on 27/11/15.
 */
public class ForgotPasswordAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkContsants {
    private Context context;
    private SweetAlertDialog pDialog;
    private UserxDTO userxDTO;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "";
    private int statusCode = 0;

    public ForgotPasswordAsyncTask(Context context, UserxDTO userxDTO){
        this.context = context;
        this.userxDTO = userxDTO;
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

    /*    list.add(new BasicNameValuePair("mobile", userxDTO.getMobile()));
        list.add(new BasicNameValuePair("realm", EggWallet.REALM));*/
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setMobile(userxDTO.getMobile());
        requestDTO.setRealm(EggWallet.REALM);

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(GET_NETWORK_IP + FORGOT_PASSWORD_URL + "?query="+ URLEncoder.encode(new Gson().toJson(requestDTO), "UTF-8"));
          //  httpGet.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpGet);
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

                    Gson gson = new Gson();
                    UserxDTO userxDTO = gson.fromJson(jsonObject.getString("response"), UserxDTO.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userxDTO", new Gson().toJson(userxDTO));
                    Intent intent = new Intent(context, ResetPasswordActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    ((ActionBarActivity)context).finish();
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
