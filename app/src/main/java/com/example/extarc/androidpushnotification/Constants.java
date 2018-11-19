package com.example.extarc.androidpushnotification;

public class Constants {

    final public static String FCM_ID = "fcm_id";
    final public static String APPLICATION_VERSION_NAME = "application_version_name";
    final public static String IS_FIRST_RUN = "is_first_run";
    final public static String USER_EMAIL_ID = "emailid";
    final public static String USER_NAME = "username";
    final public static String USER_ID = "userid";
    final public static String LOGIN_TYPE = "logintype";
    final public static String LOGIN_TYPE_MOBILE = "mobile";
    final public static String LOGIN_TYPE_FACEBOOK = "facebook";
    final public static String LOGIN_TYPE_GOOGLE = "google";
    final public static String LOGIN_TYPE_LINKEDIN = "linkedin";
    final public static String USER_DETAILS = "userdetails";
    final public static String IS_LOGIN_USER = "isloginuser";
    final public static String IS_USER_REGISTRED_NO = "N";
    final public static String JobSeekerId = "JobSeekerID";
    final public static String PROFILE_PIC_URL = "ProfilePicUrl";
    final public static String PROFILE_PIC = "ProfilePic";
    final public static String RESUME = "Resume";
    final public static String DOCUMENTS = "documents";
    final public static String AppliedJobsCount = "appliedJobsCount";
    final public static String DocsCount = "docsCount";
    final public static String ReferralCount = "ReferralCount";
    final public static String POST = "POST";
    final public static String GET = "GET";

    final public static String isFresherStr = "Fresher";
    final public static String USER_GENDER_M = "1";
    final public static String USER_GENDER_F = "2";
    final public static String USER_GENDER_MALE = "Male";
    final public static String USER_GENDER_FEMALE = "Female";
    final public static String USER_GENDER_ANY = "Any";
    final public static String JSON_USERDETAILS="UserDetails";
    final public static String JSON_USERDETAILS_PHONE_NUMBER = "MobileNumber";
    final public static String JSON_USERDETAILS_FIRST_NAME = "FirstName";
    final public static String JSON_USERDETAILS_LAST_NAME = "LastName";
    final public static String JSON_USERDETAILS_GENDER = "gender";
    final public static String JSON_USERDETAILS_DOB = "DOB";

    //Google SDK
    final public static String SERVER_BASE_URL = "SERVER_BASE_URL";
    final public static String EXCHANGE_TOKEN_URL = SERVER_BASE_URL + "/exchangetoken";
    final public static String SELECT_SCOPES_URL = SERVER_BASE_URL + "/selectscopes";
    final public static String WEB_CLIENT_ID = "WEB_CLIENT_ID";

    final public static int menu_Home = 0;
    final public static int menu_Jobs = 2;
    final public static int menu_JobsForYou=1;
    final public static int menu_JobsStatus = 3;
    final public static int menu_InterviewTips = 4;
    final public static int menu_Myprofile = 5;
    final public static int menu_Delete = 98;
    final public static int menu_Signout = 88;
    final public static int menu_Settings = 9;
    final public static int menu_ChangePassword = 77;
    final public static int menu_Contactus = 8;
    final public static int menu_UploadDocument = 6;
    final public static int menu_ReferFriend = 15;
    final public static int menu_Notifications = 7;
    final public static int menu_BulkNotifications = 28;

    //final public static int menu_RateUs = 5;

    final public static int menu_UploadResume = 11;
    final public static int menu_Languages = 21;
    final public static int menu_UploadPhoto = 12;

    final public static int menu_QuestionnaireFragment = 612;
    final public static int menu_Status_Fragment = 48;
    final public static int menu_PreferencesFragment = 30;
    final public static int menu_JobFullDescription = 50;
    final public static int menu_RateUsnext2 = 60;
    final public static int menu_RateUsnext3 = 61;
    final public static int menu_Home_Fragment = 616;
    final public static int menu_EditPersonalDetails = 601;

    final public static int menu_EditEducationalDetails = 602;
    final public static int Menu_EditExperienceDetails = 603;
    final public static int menu_EditAddressDetails = 604;
    final public static int menu_ProjectsDetails = 610;
    final public static int menu_TotalExpDetails = 611;
    final public static int menu_OthersDetails = 614;
    final public static int menu_ParentsDetails = 615;
    final public static int menu_AlertFragment = 616;
    final public static String DEVICE_ID = "deviceId";

    final public static String MasterData_GenderList = "GenderList";
    final public static String MasterData_Qualification = "QualificationList";
    final public static String MasterData_GraduationList = "GraduationList";
    final public static String MasterData_SpecializationList = "SpecializationList";
    final public static String MasterData_FunctionalList = "FunctionalList";
    final public static String MasterData_IndustryList = "IndustryList";
    final public static String MasterData_StateList = "StateList";
    final public static String MasterData_DistrictList = "DistrictList";
    final public static String MasterData_CityList = "CityList";
    final public static String MasterData_Languages = "LanguageList";
    final public static String MasterData_Universities = "UniversityList";

    final public static String JSON_EDUCATION_DETAILS = "educationDetails";
    final public static String JSON_JOB_SEEKER_DETAILS = "jsDetails";
    final public static String JSON_EXPERIENCE_DETAILS = "experienceDetails";
    final public static String JSON_PROJECT_DETAILS = "projectDetails";
    final public static String JSON_PREFERENCES_DETAILS = "preferencesDetails";
    final public static String JSON_LANGUAGE_DETAILS = "languageDetails";
    final public static String IS_MASTER_DATA_GET = "getMasterData";

    final public static String JSON_NOTIFICATION_BODY = "jsonNotificationMessage";
    private static final String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};

    private static final Integer[] YearSerialNos = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final Integer[] MonthSerialNos = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    final public static int IMAGE_CAPTURE_FLOW = 1;
    final public static int IMAGE_SELECT_FLOW = 2;
    final public static String IS_SMS_VERIFIED = "C";
    final public static String IS_SMS_NOT_VERIFIED = "P";

    final public static int IS_EXP_CURRENT = 1;

    final public static int IS_EXP_PREVIOUS = 2;

    final public static String SP_PERSISTENT_VALUES = "PersistentValues";

    final public static int COURSE_TYPE_FULL_TIME = 1;
    final public static int COURSE_TYPE_PART_TIME = 2;
    final public static int COURSE_TYPE_CORRESPONDENCE = 3;

    final public static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    final public static String APP_STORE_LINK = "https://play.google.com/store/apps/details?id=com.extarc.tmi";

    final public static String RESULT_CODE_PASSWORD_INCORRECT = "IP";
    final public static String RESULT_CODE_USER_NOT_EXIST = "IU";
    final public static String RESULT_CODE_SERVER_ERROR = "SE";
    final public static String RESULT_CODE_MULTIPLE_USERS = "MU";
    final public static String RESULT_CODE_SUCCESS = "Success";
    final public static String RESULT_CODE_INVALID_LOGIN_TYPE = "InvalidLoginType";
    final public static String CHANGE_PASSWORD_UPDATED = "1";
    final public static String CHANGE_PASSWORD_INCORRECT = "2";

    public static final String IS_REGISTERED = "isRegister";
    public static final String IS_LOCATION_SETTINGS_SHOWN = "IS_LOCATION_SETTINGS_SHOWN";

    final public static int JOB_TYPE_EVAN = 2;
    final public static int JOB_TYPE_JD = 1;
    final public static int JOB_TYPE_CLASSIFIEDS = 3;
    final public static int JOB_TYPE_GOVT = 4;

    final public static int menu_InterviewTipsFragment=33;
    final public static String AllJobsCount="allJobsCount";
    final public static String LatestJobsCount="latestJobsCount";
    final  public static String GovtJobsCount="govtJobsCount";
}