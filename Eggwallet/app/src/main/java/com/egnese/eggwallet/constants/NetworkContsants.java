package com.egnese.eggwallet.constants;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public interface NetworkContsants {
    String GET_NETWORK_IP = "http://www.eggwallet.com:3000/api";

    String REGISTER_URL = "/Userxs/register";
    String CONFIRM_OTP_URL = "/Userxs/confirmMobileOtp";
    String RESEND_OTP_URL = "/Userxs/resendMobileOtp";
    String CHECK_ACCESS_TOKEN = "/Userxs/checkAccessToken";
    String LOGIN_URL = "/Userxs/login";
    String PROFILE_UPDATE_URL = "/Userxs/updateProfile";
    String FORGOT_PASSWORD_URL = "/Userxs/forgotPassword";
    String RESET_PASSWORD_URL = "/Userxs/resetPassword";
    String CHANGE_PASSWORD_URL = "/Userxs/changePassword";
}
