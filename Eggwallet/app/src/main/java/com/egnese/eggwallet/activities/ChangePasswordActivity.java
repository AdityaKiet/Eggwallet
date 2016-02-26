package com.egnese.eggwallet.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.asyntasks.ChangePasswordAsyncTask;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.dto.MessageCustomDialogDTO;
import com.egnese.eggwallet.dto.SessionDTO;
import com.egnese.eggwallet.ui.CustomEditText;
import com.egnese.eggwallet.ui.MessageDialog;
import com.egnese.eggwallet.ui.TypefaceSpan;
import com.egnese.eggwallet.util.NetworkCheck;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adityaagrawal on 19/12/15.
 */
public class ChangePasswordActivity extends ActionBarActivity{
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.etCurrentPassword)
    CustomEditText etCurrentPassword;
    @InjectView(R.id.etNewPassword)
    CustomEditText etNewPassword;
    @InjectView(R.id.etConfirmNewPassword)
    CustomEditText etConfirmNewPassword;
    private SessionDTO sessionDTO;
    private UserxDTO userxDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_change_password);
        populate();
    }

    private void populate(){
        ButterKnife.inject(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        userxDTO = sessionDTO.getUserxDTO();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SpannableString s = new SpannableString("Change Password");
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        etConfirmNewPassword.setHint(Html.fromHtml("<small>" + getResources().getString(R.string.confirm_new_password) + "</small>"));
        etCurrentPassword.setHint(Html.fromHtml("<small>" + getResources().getString(R.string.current_password) + "</small>"));
        etNewPassword.setHint(Html.fromHtml("<small>" + getResources().getString(R.string.new_password) + "</small>"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_update) {
            if (NetworkCheck.isNetworkAvailable(this)) {
                String oldPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmNewPassword = etConfirmNewPassword.getText().toString();

                if(!newPassword.equals(confirmNewPassword)){
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.confirm_otp_auth_error));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.confirm_otp_diff_password));
                    messageCustomDialogDTO.setContext(ChangePasswordActivity.this);

                    MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                    messageCustomDialog.error();
                }else{
                    if(newPassword.length() < 6){
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setTitle(getResources().getString(R.string.confirm_otp_auth_error));
                        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(getResources().getString(R.string.confirm_otp_small_password));
                        messageCustomDialogDTO.setContext(ChangePasswordActivity.this);

                        MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                        messageCustomDialog.error();
                    }else{

                        ChangePasswordAsyncTask changePasswordAsyncTask = new ChangePasswordAsyncTask(this, userxDTO, newPassword);
                        changePasswordAsyncTask.execute();
                    }
                }


            } else {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(getResources().getString(R.string.register_activity_no_internet_title));
                messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(getResources().getString(R.string.register_activity_no_internet));
                messageCustomDialogDTO.setContext(ChangePasswordActivity.this);
                MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                messageCustomDialog.error();
            }
        }
        return false;
    }
}
