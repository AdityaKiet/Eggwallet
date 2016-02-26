package com.egnese.eggwallet.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.asyntasks.RegisterAsyncTask;
import com.egnese.eggwallet.dto.MessageCustomDialogDTO;
import com.egnese.eggwallet.dto.SessionDTO;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.ui.CustomEditText;
import com.egnese.eggwallet.ui.MessageDialog;
import com.egnese.eggwallet.ui.TypefaceSpan;
import com.egnese.eggwallet.util.NetworkCheck;
import com.google.gson.Gson;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adityaagrawal on 23/11/15.
 */
public class RegisterActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    @InjectView(R.id.etPhoneNumber)
    CustomEditText etPhoneNumber;
    @InjectView(R.id.etReferalCode)
    CustomEditText etReferalCode;
    SessionDTO sessionDTO;
    @InjectView(R.id.txtAgreement)
    TextView txtAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        populate();
    }

    private void populate() {
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        etPhoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        etReferalCode.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
       /* toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_cancel_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        SpannableString s = new SpannableString("Sign Up");
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((android.widget.TextView) toolbar.findViewById(R.id.toolbarTitle)).setText(s);

        getSupportActionBar().setTitle("");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnRegister.setBackgroundResource(R.drawable.ripple);
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = telephonyManager.getLine1Number();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);

        if (null != mPhoneNumber) {
            etPhoneNumber.setText(mPhoneNumber);
        }

        s = new SpannableString(getResources().getString(R.string.agreement_sentence));

        ClickableSpan termsSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(RegisterActivity.this, LoadURLActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("action", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan privacySpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(RegisterActivity.this, LoadURLActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("action", 2);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan contentSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(RegisterActivity.this, LoadURLActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("action", 3);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        s.setSpan(termsSpan, 40, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_steel)), 40, 57, 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 40, 57, 0);

        s.setSpan(privacySpan, 58, 73, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_steel)), 58, 73, 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 58, 73, 0);

        s.setSpan(contentSpan, 77, 94, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_steel)), 77, 94, 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 77, 94, 0);


        txtAgreement.setText(s);
        txtAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        txtAgreement.setHighlightColor(Color.TRANSPARENT);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCheck.isNetworkAvailable(RegisterActivity.this)) {
                    UserxDTO userxDTO = new UserxDTO();
                    userxDTO.setMobile(etPhoneNumber.getText().toString());
                    userxDTO.setReferralCode(etReferalCode.getText().toString());

                    if (userxDTO.getMobile().length() == 10) {
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(RegisterActivity.this, userxDTO, telephonyManager.getDeviceId(), telephonyManager.getSimCountryIso());
                        registerAsyncTask.execute();
                    } else {
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setTitle(getResources().getString(R.string.register_activity_invalid_phone_title));
                        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(getResources().getString(R.string.register_activity_invalid_phone));
                        messageCustomDialogDTO.setContext(RegisterActivity.this);
                        MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                        messageCustomDialog.error();
                    }
                } else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.register_activity_no_internet_title));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.register_activity_no_internet));
                    messageCustomDialogDTO.setContext(RegisterActivity.this);
                    MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                    messageCustomDialog.error();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_cancel) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
