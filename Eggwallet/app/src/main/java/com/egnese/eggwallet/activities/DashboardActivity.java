package com.egnese.eggwallet.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.temp.HomeFragment;
import com.egnese.eggwallet.util.FragmentDrawer;

/**
 * Created by adityaagrawal on 30/11/15.
 */
public class DashboardActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private static String TAG = DashboardActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(0);

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = "Home";
                break;
            case 1:
                fragment = new HomeFragment();
                title = "Friends";
                break;
            case 2:
                fragment = new HomeFragment();
                title = "PUVLIB";
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}





/*extends MaterialNavigationDrawer implements MaterialAccountListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init(Bundle savedInstanceState) {

        // add accounts


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SessionDTO sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        UserxDTO userxDTO = sessionDTO.getUserxDTO();
        this.setAccountListener(this);
        MaterialAccount account;
        if(userxDTO.getName() != null && !userxDTO.getName().equals(""))
            account = new MaterialAccount(this.getResources(), userxDTO.getName(), userxDTO.getMobile(), R.drawable.ic_launcher, R.mipmap.splash);
        else
            account = new MaterialAccount(this.getResources(),getResources().getString(R.string.app_name), userxDTO.getMobile(), R.drawable.ic_launcher, R.mipmap.splash);
        this.addAccount(account);


        // create sections

        this.addSection(newSection(getResources().getString(R.string.dashboard), R.mipmap.abc_dr_menu_name_mtrl_am_alpha, new DashboardFragment()));


        this.addSubheader(getResources().getString(R.string.settings));
        this.addSection(newSection(getResources().getString(R.string.profile), R.mipmap.abc_dr_menu_profile_mtrl_am_alpha, new Intent(this, ProfileUpdateActivity.class)));
        this.addSection(newSection(getResources().getString(R.string.change_password), R.mipmap.abc_dr_menu_name_mtrl_am_alpha, new Intent(this, ChangePasswordActivity.class)));


        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);
        this.closeDrawer();
    }

    @Override
    public void onAccountOpening(MaterialAccount account) {

    }

    @Override
    public void onChangeAccount(MaterialAccount newAccount) {

    }
}
*/