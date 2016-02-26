package com.egnese.eggwallet.dashboardfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egnese.eggwallet.R;

/**
 * Created by adityaagrawal on 12/12/15.
 */
public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

}