package edu.berziet.houserental;

public class ApiUrl {
    private static final String API_ROOT_URL = "http://192.168.10.4:8081/";

    public static final String CONNECT_URL = API_ROOT_URL.concat("connect");
    public static final String SIGN_IN_URL = API_ROOT_URL.concat("login");
    public static final String AGENCIES_URL = API_ROOT_URL.concat("agencies");
    public static final String TENANTS_URL = API_ROOT_URL.concat("tenants");
    public static final String TENANT_PROFILE_URL = API_ROOT_URL.concat("tenants/%s");
    public static final String AGENCY_PROFILE_URL = API_ROOT_URL.concat("agencies/%s");
    public static final String AGENCY_PROPERTIES_LIST_URL = API_ROOT_URL.concat("agencies/%s/properties");
    public static final String PROPERTIES_URL = API_ROOT_URL.concat("properties/");
    public static final String PROPERTIES_SEARCH_URL = API_ROOT_URL.concat("properties/search");
    public static final String PROPERTY_URL = API_ROOT_URL.concat("properties/%d");
    public static final String PROPERTY_IMAGES_URL = API_ROOT_URL.concat("properties/%d/images");
    public static final String IMAGE_URL = API_ROOT_URL.concat("images/%s");
    public static final String PROPERTY_IMAGE_ID = API_ROOT_URL.concat("images/%d");

    /*
    Rental Requests
     */
    public static final String RENTAL_REQUESTS_URL = API_ROOT_URL.concat("requests/");
    public static final String TENANT_RENTAL_REQUESTS_URL = API_ROOT_URL.concat("requests/tenant/%s");
    public static final String TENANT_RENTAL_REQUESTS_FOR_PROPERTY_CHECK_URL = API_ROOT_URL.concat("requests/tenant/%s/%d");
    public static final String AGENCY_RENTAL_REQUESTS_URL = API_ROOT_URL.concat("requests/agency/%s");
    public static final String APPROVE_RENTAL_REQUESTS_URL = API_ROOT_URL.concat("requests/%d/approve");
    public static final String REJECT_RENTAL_REQUESTS_URL = API_ROOT_URL.concat("requests/%d/reject");

    public static final String TENANT_RENTAL_HISTORY_URL = API_ROOT_URL.concat("rentalhistory/tenant/%s");
    public static final String AGENCY_RENTAL_HISTORY_URL = API_ROOT_URL.concat("rentalhistory/agency/%s");

}
