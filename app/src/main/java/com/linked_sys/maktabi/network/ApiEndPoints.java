package com.linked_sys.maktabi.network;

public class ApiEndPoints {

    public static final String BASE_URL = "http://dev.maktaby.org";

    public static final String TOKEN = "/token";
    public static final String GET_USER_DATA = "/API/Login/GetUserData";
    public static final String GET_CAPTAIN_BALANCE = "/API/Balance/GetCaptainBalance";
    public static final String GET_CAPTAIN_COMPLAINTS = "/API/Complains/GetCaptainComplains";
    public static final String GET_CAPTAIN_COMPLAINTS_Details = "/API/Complains/GetComplainDetailsAndReplys";
    public static final String POST_REPLY = "/API/Complains/PostReply";
    public static final String POST_COMPLAINT = "/API/Complains/PostComplains";
    public static final String SEND_FB_TOKEN = "/API/DeviceToken/SetDeviceToken";
    public static final String REMOVE_FB_TOKEN = "/API/DeviceToken/RemoveToken";
}
