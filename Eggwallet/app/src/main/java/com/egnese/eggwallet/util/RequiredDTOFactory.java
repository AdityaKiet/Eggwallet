package com.egnese.eggwallet.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.egnese.eggwallet.constants.EggWallet;
import com.egnese.eggwallet.dto.RequiredDTO;
import com.egnese.eggwallet.dto.SessionDTO;
import com.egnese.eggwallet.dto.UserxDTO;
import com.google.gson.Gson;

/**
 * Created by adityaagrawal on 25/12/15.
 */
public class RequiredDTOFactory {

    public static RequiredDTO getObject(Context context){
        RequiredDTO requiredDTO = new RequiredDTO();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SessionDTO sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        UserxDTO userxDTO = sessionDTO.getUserxDTO();

        requiredDTO.setId(userxDTO.getId());
        requiredDTO.setAccessToken(userxDTO.getAccessToken());
        requiredDTO.setRealm(EggWallet.REALM);
        return requiredDTO;
    }
}
