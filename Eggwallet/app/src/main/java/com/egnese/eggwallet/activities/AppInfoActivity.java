package com.egnese.eggwallet.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.andraskindler.parallaxviewpager.ParallaxViewPager;
import com.egnese.eggwallet.R;
import com.egnese.eggwallet.adapters.CustomPagerAdapter;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adityaagrawal on 23/11/15.
 */
public class AppInfoActivity extends ActionBarActivity{
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    private CustomPagerAdapter mCustomPagerAdapter;
    @InjectView(R.id.pager)
    ParallaxViewPager mViewPager;
    @InjectView(R.id.circleIndicatorAppInfo)
    ImageView circleIndicatorAppInfo;
    private static int currentPage = 0;
    private static int NUM_PAGES = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        populate();
    }


    private void populate(){
        ButterKnife.inject(this);
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_1);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setBackgroundResource(R.drawable.ic_launcher);
        mViewPager.setScaleType(ParallaxViewPager.FIT_WIDTH);
        mViewPager.setOverlapPercentage(0.147f);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            btnRegister.setBackgroundResource(R.drawable.ripple);
            btnLogin.setBackgroundResource(R.drawable.ripple);

        }
        createFolder();
        btnLogin.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/LatoLatin-Regular.ttf"));
        btnRegister.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/LatoLatin-Regular.ttf"));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_1);
                        break;
                    case 1:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_2);
                        break;
                    case 2:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_3);
                        break;
                    case 3:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_4);
                        break;
                    case 4:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_5);
                        break;
                    case 5:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_6);
                        break;
                    case 6:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_7);
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


       /* final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
                switch (currentPage) {
                    case 0:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_1);
                        break;
                    case 1:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_2);
                        break;
                    case 2:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_3);
                        break;
                    case 3:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_4);
                        break;
                    case 4:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_5);
                        break;
                    case 5:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_6);
                        break;
                    case 6:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_7);
                        break;
                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });*/


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppInfoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppInfoActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void createFolder() {

        try {
            File folder = getExternalFilesDir("log");
            if (!folder.exists()) {
                folder.mkdir();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
