package com.egnese.eggwallet.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.asyntasks.EnterProfileAsyncTask;
import com.egnese.eggwallet.dto.ProfileDTO;
import com.egnese.eggwallet.dto.RequiredDTO;
import com.egnese.eggwallet.dto.SessionDTO;
import com.egnese.eggwallet.dto.UserxDTO;
import com.egnese.eggwallet.ui.CustomEditText;
import com.egnese.eggwallet.ui.TypefaceSpan;
import com.egnese.eggwallet.util.NetworkCheck;
import com.egnese.eggwallet.util.RequiredDTOFactory;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adityaagrawal on 24/12/15.
 */
public class EnterProfileActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.txtUpdatProfileTitle)
    TextView txtUpdatProfileTitle;
    @InjectView(R.id.etName)
    CustomEditText etName;
    @InjectView(R.id.etEmail)
    CustomEditText etEmail;
    @InjectView(R.id.txtDOB)
    com.neopixl.pixlui.components.textview.TextView txtDOB;
    @InjectView(R.id.txtSelectCity)
    com.neopixl.pixlui.components.textview.TextView txtSelectCity;
    @InjectView(R.id.btnContinue)
    Button btnContinue;
    private SessionDTO sessionDTO;
    private UserxDTO userxDTO;
    private String dob = "";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_profile);
        populate();
    }

    private void populate() {
        ButterKnife.inject(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        userxDTO = sessionDTO.getUserxDTO();

        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("Complete Profile");
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);


        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            btnContinue.setBackgroundResource(R.drawable.ripple);
        }
        s = new SpannableString("BASIC INFORMATION");
        s.setSpan(new TypefaceSpan(this, "ProximaNova-Semibold.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtUpdatProfileTitle.setText(s);
        etName.setHint(Html.fromHtml("<small>" + "Name" + "</small>"));
        etEmail.setHint(Html.fromHtml("<small>" + "Email" + "</small>"));

        if (userxDTO.getName() != null)
            etName.setText(userxDTO.getName());
        if (userxDTO.getEmail() != null)
            etEmail.setText(userxDTO.getEmail());
        if (userxDTO.getDob() != null) {
            SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outFmt = new SimpleDateFormat("dd-MMMM-yyyy");
            try {
                dob = userxDTO.getDob();
                txtDOB.setText(outFmt.format(inFmt.parse(dob)));
                txtDOB.setAlpha(1.0f);
                txtDOB.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(EnterProfileActivity.this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        txtSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(EnterProfileActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {

                } catch (GooglePlayServicesNotAvailableException e) {

                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequiredDTO requiredDTO = RequiredDTOFactory.getObject(EnterProfileActivity.this);
                ProfileDTO profileDTO = new ProfileDTO();
                profileDTO.setName(etName.getText().toString());
                profileDTO.setEmail(etEmail.getText().toString());
                profileDTO.setDob(txtDOB.getText().toString());
                profileDTO.setAddress(txtSelectCity.getText().toString());
                profileDTO.setGender("Male");
                EnterProfileAsyncTask enterProfileAsyncTask = new EnterProfileAsyncTask(EnterProfileActivity.this, requiredDTO, profileDTO);
                enterProfileAsyncTask.execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_enter_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_skip) {
            if (NetworkCheck.isNetworkAvailable(this)) {
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                SpannableString s = new SpannableString(place.getAddress());
                s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtSelectCity.setAlpha(1.0f);
                txtSelectCity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                txtSelectCity.setText(s);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
}
