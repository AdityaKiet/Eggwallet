package com.egnese.eggwallet.asyntasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.constants.NetworkContsants;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.dto.ErrorDTO;
import com.egnese.eggwallet.dto.MessageCustomDialogDTO;
import com.egnese.eggwallet.dto.ResponseDTO;
import com.egnese.eggwallet.dto.SessionDTO;
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
 * Created by adityaagrawal on 19/12/15.
 */
public class ChangePasswordAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkContsants {
    private Context context;
    private SweetAlertDialog pDialog;
    private UserxDTO userxDTO;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "", newPassword;
    private int statusCode = 0;

    public ChangePasswordAsyncTask(Context context, UserxDTO userxDTO, String newPassword) {
        this.context = context;
        this.userxDTO = userxDTO;
        this.newPassword = newPassword;
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

        list.add(new BasicNameValuePair("id", userxDTO.getId()));
        list.add(new BasicNameValuePair("newPassword", newPassword));
        list.add(new BasicNameValuePair("oldPassword", userxDTO.getPassword()));
        list.add(new BasicNameValuePair("accessToken", userxDTO.getAccessToken()));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + CHANGE_PASSWORD_URL);

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
            JSONObject jsonObject = new JSONObject(result);
            if (exceptionToBeThrown != null) {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(exceptionToBeThrown.getMessage());
                messageCustomDialogDTO.setContext(context);
                MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                messageCustomDialog.error();

            } else {
                if (statusCode >= 200 && statusCode <= 299) {
                    ResponseDTO responseDTO = new Gson().fromJson(jsonObject.getString("response"), ResponseDTO.class);
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(context.getString(R.string.success));
                    messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(responseDTO.getMessage());
                    messageCustomDialogDTO.setContext(context);

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(messageCustomDialogDTO.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText(messageCustomDialogDTO.getTitle()).setContentText(messageCustomDialogDTO.getMessage());
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ((ActionBarActivity)context).finish();
                        }
                    });
                    sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ((ActionBarActivity)context).finish();
                        }
                    });
                    sweetAlertDialog.show();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    SessionDTO sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
                    userxDTO.setPassword(newPassword);
                    sessionDTO.setUserxDTO(userxDTO);
                    editor.putString("session", gson.toJson(sessionDTO));
                    editor.commit();

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
