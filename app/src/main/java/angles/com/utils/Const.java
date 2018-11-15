package angles.com.utils;

public class Const {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    public static int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    public static final int STORAGE_PERMISSION_CODE = 102;

    // name
    public static String PREF_NAME = "Angellift";
    //mobile type
    public static final int MOBILETYPE = 1;
    // payment mode
    public static final int CASH = 1;
    public static final int CREDIT = 0;
    public static final String URL = "url";

    // no request id
    public static final int NO_REQUEST = -1;
    public static final int NO_TIME = -1;
    public static final String MANUAL = "manual";

    public class UrlClient {
        public static final String BASE_URL = "http://192.168.137.6/angellift/index.php/api/";
        public static final String BASE_URL_IMG = BASE_URL+"assets/upload/driverwithcar/";
        public static final String REGISTER_URL = BASE_URL + "Dw_register_api/index";
        public static final String LOGIN_WITHCAR_URL = BASE_URL + "Login_module_api/driver_with_car";
        public static final String CHANGE_PASSWORD_URL = BASE_URL + "Change_password_api/";
        public static final String GET_PROFILE_DATA = BASE_URL + "profile/Dw_profile_api/index/";
        public static final String SET_PROFILE_DATA = BASE_URL + "profile/Dw_profile_edit_api/index/";
        public static final String PROFILE_IMG = BASE_URL_IMG + "dw_image/";


    }

    public class Params {

        // get profile
        public static final String PROFILEDETAILS = "profiledetails";
        public static final String PROFILE_UPDATE = "profile_update";


        //change password
        public static final String OLD = "old";
        public static final String NEW = "new";
        public static final String NEW_CONFIRM = "new_confirm";
        public static final String USER_ID = "user_id";

        //register
        public static final String DW_MOBILENO = "dw_mobileno";
        public static final String DW_NAME = "dw_name";
        public static final String DW_EMAIL = "dw_email";
        public static final String DW_ADDRESS = "dw_address";
        public static final String DW_LICENSE_NUMBER = "dw_license_number";
        public static final String DW_LICENSE_PICTURE = "dw_license_picture";
        public static final String DW_LC_DATE = "dw_lc_date";
        public static final String DW_EXPIRE_DATE = "dw_expire_date";
        public static final String DW_CAR_NO = "dw_car_no";
        public static final String DW_CAR_DOCUMENT = "dw_car_document";
        public static final String DW_MODEL = "dw_model";
        public static final String DW_TYPE = "dw_type";
        public static final String PASSWORD = "password";
        public static final String MOBILE_TYPE = "mobile_type";
        public static final String DW_DOB = "dw_dob";
        public static final String DW_CAR_IMAGE = "dw_car_image";


        // login
        public static final String IDENTITY = "identity";
        public static final String MOBILE_DEVICE_ID = "mobile_device_id";
        public static final String MOBILE_TOKEN = "mobile_token";

        // login response
        public static final String TRUE = "true";
        public static final String STATUS = "status";
        public static final String DATA = "data";
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String FIRST_NAME = "first_name";
        public static final String TOKEN_ID = "token_id";
        public static final String ROLE_ID = "role_id";
        public static final String DW_USERDETAILS_ID = "dw_userdetails_id";
        public static final String DV_USERDETAILS_ID = "dv_userdetails_id";
        public static final String DW_ID = "dw_id";
        public static final String DW_IMAGE = "dw_image";

        // other
        public static final String ROUTES = "routes";
        public static final String LEGS = "legs";
        public static final String TEXT = "text";
        public static final String VALUE = "value";
        public static final String DURATION = "duration";
        public static final String START_ADDRESS = "start_address";
        public static final String END_ADDRESS = "end_address";
        public static final String START_LOCATION = "start_location";
        public static final String END_LOCATION = "end_location";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "lng";
        public static final String STEPS = "steps";
        public static final String POLYLINE = "polyline";
        public static final String POINTS = "points";
        public static final String HTML_INSTRUCTIONS = "html_instructions";
        public static final String PHONE_CODE = "phone-code";
        public static final String NAME = "name";
        public static final String DEST_LATITUDE = "dest_latitude";
        public static final String DEST_LONGITUDE = "dest_longitude";
        public static final String KMS = "kms";
        public static final String CARD_DETAILS = "card_details";
        public static final String CARD_ID = "card_id";

        public static final String BEARING = "bearing";
        public static final String DISTANCE = "distance";
    }

    // Placesurls
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String TYPE_NEAR_BY = "/nearbysearch";
    public static final String OUT_JSON = "/json";
}
