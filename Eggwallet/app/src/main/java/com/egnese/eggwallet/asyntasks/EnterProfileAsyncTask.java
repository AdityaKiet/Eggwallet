package com.egnese.eggwallet.asyntasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.activities.DashboardActivity;
import com.egnese.eggwallet.constants.NetworkContsants;
import com.egnese.eggwallet.dto.ErrorDTO;
import com.egnese.eggwallet.dto.MessageCustomDialogDTO;
import com.egnese.eggwallet.dto.ProfileDTO;
import com.egnese.eggwallet.dto.RequiredDTO;
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
 * Created by adityaagrawal on 25/12/15.
 */
public class EnterProfileAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkContsants {
    private Context context;
    private SweetAlertDialog pDialog;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "";
    private int statusCode = 0;
    private ProfileDTO profileDTO;
    private RequiredDTO requiredDTO;

    public EnterProfileAsyncTask(Context context, RequiredDTO requiredDTO, ProfileDTO profileDTO) {
        this.context = context;
        this.requiredDTO = requiredDTO;
        this.profileDTO = profileDTO;
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
        Gson gson = new Gson();
        list.add(new BasicNameValuePair("required", gson.toJson(requiredDTO)));
        list.add(new BasicNameValuePair("data", gson.toJson(profileDTO)));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + PROFILE_UPDATE_URL);

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
                messageCustomDialogDTO.setMessage(exceptionToBeThrown.getMessage() + exceptionToBeThrown.toString());
                messageCustomDialogDTO.setContext(context);
                MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                messageCustomDialog.error();

            } else {

                JSONObject jsonObject = new JSONObject(result);
                if (statusCode >= 200 && statusCode <= 299) {

                    UserxDTO userxDTO = new Gson().fromJson(jsonObject.getString("response"), UserxDTO.class);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Gson gson = new Gson();
                    SessionDTO sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
                    sessionDTO.setUserxDTO(userxDTO);
                    editor.putString("session", gson.toJson(sessionDTO));
                    editor.putBoolean("isLogin", true);
                    editor.commit();

                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
        } catch (Exception e) {
            e.printStackTrace();
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
