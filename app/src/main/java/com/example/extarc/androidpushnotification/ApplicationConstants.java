package com.example.extarc.androidpushnotification;

public interface ApplicationConstants {
    //*** live webservice details
     // static final String SERVER_PATH = "http://www.jobsdialog.com/jdwebservices/jbsdservice.svc";
     // static final String SERVER_IMAGE_PATH = "http://www.jobsdialog.com/jobseeker";

    //*** Azure webservice details
   //  static final String SERVER_PATH = "http://extarc-vm.cloudapp.net/JDWebService/jbsdservice.svc";
   // static final String SERVER_IMAGE_PATH = "http://extarc-vm.cloudapp.net/JDWebService/images";
    //static final String SERVER_PATH = "http://extarc-vm.cloudapp.net/JDWebServiceLocal/jbsdservice.svc";

    // cloud path
   //  static final String SERVER_PATH = "http://ec2-13-233-247-0.ap-south-1.compute.amazonaws.com:8080/Notifications/";
    // static final String SERVER_IMAGE_PATH = "http://ec2-3-16-123-197.us-east-2.compute.amazonaws.com:8080/Notifications/";
// local path
   static final String SERVER_PATH = "http://192.168.1.6:8080/Notifications/";
   static final String SERVER_IMAGE_PATH = "http://192.168.1.6:8080/Notifications/";

    static final String CLIENT_LOGO_PATH = "http://beta.jobsdialog.com/smewebsite";
    // Google Project Number
    static final String GOOGLE_PROJ_ID = "310325112624";
    static final String GOOGLE_PROJ_KEY = "AIzaSyCBbGphg5oVp8Fsr_nzmBvfSmcaS5vv-O0";
    static final String MSG_KEY = "message";

    static final String Notification_Action_Pre_reg = "Pre-registration";
    static final String Notification_Action_Edit_Profile="EditProfile";
    static final String Notification_Action_QuestionnaireForILA="QuestionnaireForILA";
    public static String NOTIFICATION_TYPE_WELCOME_BEFORE_REG = "WelcomeBeforeRegistration";
    public static String NOTIFICATION_TYPE_COMPLETE_PROFILE = "CompleteProfile";
    public static String NOTIFICATION_TYPE_INTERVIEW_TIPS = "InterviewTips";
    public static String NOTIFICATION_TYPE_QUESTIONNAIRE = "QuestionnaireForILA";
    public static String NOTIFICATION_TYPE_INFORM_STATUS_CHANGE = "InformStatusChange";
    static final String NOTIFICATION_TYPE_BULKNOTIFICATION = "BulkNotification";
    static final String NOTIFICATION_TYPE_INFORMNOTIFICATION = "InformNotification";


    static final String MIXPANEL_KEY = "57d5ae4dabfda8e397c4e05eaf19534c";

    String NOTIFICATION_TYPE_JOBS_FOR_YOU ="JobsForYou" ;
}