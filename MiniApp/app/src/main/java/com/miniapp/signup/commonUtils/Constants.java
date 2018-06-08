package com.miniapp.signup.commonUtils;



/**
 * Created by mkaur
 */
public class Constants {
    public static final String TAG = "SignupModule";
    // dialog messages
    public static final String kDialogMsgForSignup = "Signing Up...";
    public static final String kDialogMsgForUnMatchPassword = "Both passwords are not the same.";
    public static final String kVerifyPhoneNumMsg = "Kindly verify your number before signup.";
    public static final String kKeyForPhoneNumber = "Phone_Number";
    public static final String kKeyForSettings= "Settings";
    public static final String kMsgForInActiveAccount = "Your account is in-active. Verify your account first.";
    public static final String kMsgForTokenExpired = "Token has expired";
    public static final String kMsgForYourTokenExpired = "Your token is expired.";
    public static final String kMsgForSessionExpired = "Your session expired. Kindly login again.";

    public static class Gender {
        public static final String CHOOSE = "Select your gender";
        public static final String MALE = "Male";
        public static final String FEMALE = "Female";
        public static final String OTHERS = "Others";
    }
    public static class UserType {
        public static final String CHOOSE = "Select your type";
        public static final String HIRE = "Want to Hire";
        public static final String FREELANCER = "Freelancer";
    }
}
