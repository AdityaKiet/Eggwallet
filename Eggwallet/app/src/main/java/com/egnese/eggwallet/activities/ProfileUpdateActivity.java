package com.egnese.eggwallet.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.asyntasks.ProfileUpdateAsyncTask;
import com.egnese.eggwallet.dto.MessageCustomDialogDTO;
import com.egnese.eggwallet.dto.SessionDTO;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.ui.CustomEditText;
import com.egnese.eggwallet.ui.MessageDialog;
import com.egnese.eggwallet.ui.TypefaceSpan;
import com.egnese.eggwallet.util.NetworkCheck;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileUpdateActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.txtUpdatProfileTitle)
    TextView txtUpdatProfileTitle;
    @InjectView(R.id.etName)
    CustomEditText etName;
    @InjectView(R.id.etPhoneNumber)
    CustomEditText etPhoneNumber;
    @InjectView(R.id.etEmail)
    CustomEditText etEmail;
    @InjectView(R.id.txtDOB)
    com.neopixl.pixlui.components.textview.TextView txtDOB;
    @InjectView(R.id.txtSelectCity)
    com.neopixl.pixlui.components.textview.TextView txtSelectCity;
    private SessionDTO sessionDTO;
    private UserxDTO userxDTO;
    private String dob = "";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        populate();
    }


    private void populate() {
        ButterKnife.inject(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        userxDTO = sessionDTO.getUserxDTO();


        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SpannableString s = new SpannableString("Edit Profile");
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        s = new SpannableString("BASIC INFORMATION");
        s.setSpan(new TypefaceSpan(this, "ProximaNova-Semibold.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtUpdatProfileTitle.setText(s);
        etName.setHint(Html.fromHtml("<small>" + "Name" + "</small>"));
        etEmail.setHint(Html.fromHtml("<small>" + "Email" + "</small>"));
        etPhoneNumber.setHint(Html.fromHtml("<small>" + "Phone Number" + "</small>"));

        etPhoneNumber.setFocusable(false);
        etPhoneNumber.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        etPhoneNumber.setClickable(false); // user navigates with wheel and selects widget

        if (userxDTO.getName() != null)
            etName.setText(userxDTO.getName());
        if (userxDTO.getEmail() != null)
            etEmail.setText(userxDTO.getEmail());
        if (userxDTO.getMobile() != null){
            etPhoneNumber.setText(userxDTO.getMobile());
        }
            etPhoneNumber.setText(userxDTO.getMobile());
        if (userxDTO.getDob() != null) {
            SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outFmt = new SimpleDateFormat("dd-MMMM-yyyy");
            try {
                dob = userxDTO.getDob();
                txtDOB.setText(outFmt.format(inFmt.parse(dob)));
                txtDOB.setAlpha(1.0f);
                txtDOB.setTextSize(TypedValue.COMPLEX_UNIT_PX, etPhoneNumber.getTextSize());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        StringBuffer dobBuffer = new StringBuffer();
                        dobBuffer.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat outFmt = new SimpleDateFormat("dd-MMMM-yyyy");
                        try {
                            dob = dobBuffer.toString();
                            txtDOB.setText(outFmt.format(inFmt.parse(dobBuffer.toString())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileUpdateActivity.this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        txtSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(ProfileUpdateActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {

                } catch (GooglePlayServicesNotAvailableException e) {

                }
            }
        });

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
                userxDTO.setName(etName.getText().toString().trim());
                userxDTO.setEmail(etEmail.getText().toString().trim());
                if(txtSelectCity.getText().toString().equals("Select City"))
                    userxDTO.setCity("");
                else
                    userxDTO.setCity(txtSelectCity.getText().toString());
                if (!txtDOB.getText().toString().equals("Date of Birth")) {
                    userxDTO.setDob(dob);
                } else {

                    userxDTO.setDob("");
                }
                ProfileUpdateAsyncTask profileUpdateAsyncTask = new ProfileUpdateAsyncTask(this, userxDTO);
                profileUpdateAsyncTask.execute();
            }
            else{
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(getResources().getString(R.string.register_activity_no_internet_title));
                messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(getResources().getString(R.string.register_activity_no_internet));
                messageCustomDialogDTO.setContext(ProfileUpdateActivity.this);
                MessageDialog messageCustomDialog = new MessageDialog(messageCustomDialogDTO);
                messageCustomDialog.error();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                SpannableString s = new SpannableString(place.getName() + " , " + place.getAddress());
                s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtSelectCity.setAlpha(1.0f);
                txtSelectCity.setTextSize(TypedValue.COMPLEX_UNIT_PX, etPhoneNumber.getTextSize());
                txtSelectCity.setText(s);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

}
