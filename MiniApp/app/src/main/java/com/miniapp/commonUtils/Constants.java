package com.miniapp.commonUtils;


/**
 * Created by chicmic on 10/11/17.
 */
public class Constants {

    public static final String LOG_TAG = "app";
    public static final String kPakageTag = "package";
    public static final String kPakageName = "com.miniapp";
    public static final String DEVICE_TYPE = "android";
    public static final String UPDATE_TEXT = "Update";
    public static final String EDIT_TEXT = "Edit";
    public static final int kAdPosition = 4;
    public static final int SHOWING_COUNT = 8;
    public static final int END_POSITION_CONST = 2;
    public static final int kAdDivisor = 5;
    public static final String kMsgForExit = "Press again to exit.";

    public static final int GALLERY_PERMISSIONS_REQUEST = 0;
    public static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int GALLERY_VIDEO_REQUEST = 4;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    public static final String kKeyForTitle = "title";
    public static final String kKeyForJobId = "JOBID";
    public static final String kKeyForDescription = "description";
    public static final String kKeyForUserDetails = "UserDetails";
    public static final String kKeyForCategoryId = "category_id";
    public static final String kKeyForCategoryName = "category_name";
    public static final String kKeyForSubCategory = "sub_category";
    public static final String kKeyForSubCategoryId = "sub_category_id";
    public static final String kKeyForUseSubCategory = "user_subcategory";
    public static final String kKeyForJobsCategories = "JobsCategories";
    public static final String kKeyForSelectedSubJobsCategories = "SelectedSubJobsCategories";
    public static final String kKeyForSelectedJobsCategory = "SelectedJobsCategoriY";
    public static final String kKeyForImageUrl = "image";
    public static final String kKeyForImagesArrayUrl = "images[]";
    public static final String kKeyForFileToUpload = "file_to_upload";
    public static final String kKeyForImageBitmap = "image_bitmap";
    public static final String kKeyForCreatedTime = "created_at";
    public static final String kKeyForPhoneNum = "phone_no";
    public static final String kKeyForProposalId = "jobid";
    public static final String kKeyForProposalAmount = "budget";
    public static final String kKeyForSubject = "subject";
    public static final String kKeyForMessage = "message";
    public static final String kKeyForEmail = "email";
    public static final String kKeyForVideoUrl = "video_url";
    public static final String kKeyForFacebookId = "idFacebook";
    public static final String kKeyForAuthToken = "token";
    public static final String kKeyForIsLoggedIn = "IsLoggedIn";
    public static final String kKeyForUserId = "user_id";
    public static final String kKeyForUserStatus = "user_status";
    public static final String kKeyForUsername = "user_name";
    public static final String kKeyForUserType = "user_type";
    public static final String kKeyForBiography = "biography";
    public static final String kKeyForUserGender = "gender";
    public static final String kKeyForUserProfile = "picture";
    public static final String kKeyForDeviceToken = "deviceToken";
    public static final String kKeyForReloadHomeList = "shouldReloadHomeList";
    public static final String kKeyForLikeUsOnFb = "LikeUsOnFb";
    public static final String kKeyForDeviceType = "deviceType";
    public static final String ACCESS_TOKEN = "accesstoken";
    public static final int kKeyStatusCodeOk = 200;
    public static final String kMsgForAppPermissions = "Kindly allow location permission to MyLikePages.";
    public static final String kMsgForCategorySelection = "Kindly select a category.";
    public static final String kMsgForConfirmLogout = "Do you really want to logout.";
    public static final String INTENT_FILTER = "INTENT_FILTER";
    public static final String CHAT_TYPE = "1000";

    // dialog messages
    public static final String kMsgForLogin = "Logging in...";
    public static final String kMsgForInValidEmail = "Please enter a valid email.";
    public static final String kMsgForFailedLogin = "Failed to login. Try again after some time.";

    public enum FragmentAction {
        ADD,
        REPLACE,
        REMOVE
    }

    public static class Calls {
        public static final int kKeyForLogin = 700;
        public static final int kKeyForLogout = 701;
        public static final int kKeyForSignup = 702;
        public static final int kKeyForForgotPassowrd = 703;
    }

    public static class ButtonPressedStatus {
        public static final String SimpleLogin = "SimpleLogin";
        public static final String FBLogin = "FBLogin";
        public static final String TwitterLogin = "TwitterLogin";
        public static final String GoogleLogin = "GoogleLogin";
    }

    public static class ServiceConstants {
        //        public static final String SERVER_URL = "http://productionmini.com/public/api/";
        public static final String SERVER_URL = "http://productionmini.com/mini/api/";
        public static final String SERVER_URL2 = "http://productionmini.com/mini/api/";

        public static final String LOGIN_URL_EXTRA = "login.php";
        public static final String LOGIN_FB_URL_EXTRA = "social-fb-login.php";
        public static final String LOGIN_GOOGLE_URL_EXTRA = "social-google-login.php";
        public static final String LOGOUT_URL_EXTRA = "logout.php";
        public static final String REGISTER_URL_EXTRA = "signup.php";
        public static final String FORGOT_PASSWORD_URL_EXTRA = "forgot-password.php";
        public static final String UPDATE_USER_TYPE_URL_EXTRA = "update-user-type.php";
        public static final String GET_PROFILE_URL_EXTRA = "profile.php";
        public static final String EDIT_PROFILE_URL_EXTRA = "edit-profile.php";
        public static final String FETCH_FREELANCERS_URL_EXTRA = "users-list.php?user_type=1";
        public static final String EDIT_OTHER_IMAGES_URL_EXTRA = "edit-other-images.php";
        public static final String DELETE_OTHER_IMAGES_URL_EXTRA = "delete-other-image.php";
        public static final String CONTACT_US_URL_EXTRA = "contact-us.php";
        public static final String SEARCH_URL_EXTRA = "get-jobs.php?search=";
        public static final String GET_JOBS_URL_EXTRA = "get-jobs.php";
        public static final String POST_JOB_URL_EXTRA = "post-job.php";
        public static final String GET_JOB_CATEGORIES_URL_EXTRA = "get-categories.php";
        public static final String GET_JOB_SUBCATEGORIES_URL_EXTRA = "get-subcategories.php?category_id=";
        public static final String SEND_PROPOSAL_URL_EXTRA = "addproposal.php";
        public static final String GET_PROPOSALS_URL_EXTRA = "proposallist.php?id=3";
        public static final String AWARD_PROPOSAL_URL_EXTRA = "jobs/get-subcategories?category_id=";
        public static final String CANCEL_AWARDED_PROPOSAL_URL_EXTRA = "jobs/get-subcategories?category_id=";
        public static final String GET_WALLET_URL_EXTRA = "wallet.php";

//        public static final String LOGIN_URL_EXTRA = "users/login";
//        public static final String LOGIN_FB_URL_EXTRA = "users/social-fb-login";
//        public static final String LOGIN_GOOGLE_URL_EXTRA = "users/social-google-login";
//        public static final String LOGOUT_URL_EXTRA = "users/logout";
//        public static final String REGISTER_URL_EXTRA = "users/signup";
//        public static final String FORGOT_PASSWORD_URL_EXTRA = "users/forgot-password";
//        public static final String GET_PROFILE_URL_EXTRA = "users/profile";
//        public static final String EDIT_PROFILE_URL_EXTRA = "users/edit-profile";
//        public static final String FETCH_FREELANCERS_URL_EXTRA = "users/users-list?user_type=1";
//        public static final String EDIT_OTHER_IMAGES_URL_EXTRA = "users/edit-other-images";
//        public static final String DELETE_OTHER_IMAGES_URL_EXTRA = "users/delete-other-image";
//        public static final String CONTACT_US_URL_EXTRA = "users/contact-us";
//        public static final String SEARCH_URL_EXTRA = "jobs/get-jobs?search=";
//        public static final String GET_JOBS_URL_EXTRA = "jobs/get-jobs";
//        public static final String POST_JOB_URL_EXTRA = "jobs/post-job";
//        public static final String GET_JOB_CATEGORIES_URL_EXTRA = "jobs/get-categories";
//        public static final String GET_JOB_SUBCATEGORIES_URL_EXTRA = "jobs/get-subcategories?category_id=";
//        public static final String SEND_PROPOSAL_URL_EXTRA = "addproposal.php";
//        public static final String GET_PROPOSALS_URL_EXTRA = "proposallist.php";
//        public static final String AWARD_PROPOSAL_URL_EXTRA = "jobs/get-subcategories?category_id=";
//        public static final String CANCEL_AWARDED_PROPOSAL_URL_EXTRA = "jobs/get-subcategories?category_id=";
//        public static final String GET_WALLET_URL_EXTRA = "jobs/get-subcategories?category_id=";

        public static enum REQUEST_TYPE {
            GET,
            POST,
            GET_SPL
        }
    }

    public static class ResponseStatus {
        public static final int SUCCESS = 200;
        public static final String Status_Message = "status_message";
        public static final String auth_token_not_valid_msg = "auth token not valid";
        public static final String SESSION_EXPIRED = "Session expired. Kindly login again.";
        public static final String BAD_REQUEST = "400";
        public static final String INVALID_TOKEN = "401";
        public static final String INVALID_DATA = "402";
        public static final String USER_NOT_FOUND = "403";
    }

    public static final String kKeyForOk = "Ok";
    public static final String kKeyForCancel = "Cancel";
}
