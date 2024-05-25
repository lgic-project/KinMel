package com.example.kinmel.StaticFiles;

public class ApiStatic {
    public static final String USER_REGISTRATION_API = "http://192.168.1.67:8080/kinMel/users/register";
    public static final String REGENERATE_OTP_API = "http://192.168.1.67:8080/kinMel/regenerate-otp";
    public static final String VERIFY_ACCOUNT_API = "http://192.168.1.67:8080/kinMel/verify-account";
    public static final String LOGIN_ACCOUNT_API = "http://192.168.1.67:8080/kinMel/login";
    public static final String FETCH_PRODUCT_HOME_API = "http://192.168.1.67:8080/kinMel/products";
    public static final String FETCH_USER_DETAIL = "http://192.168.1.67:8080/kinMel/user";
    public static String getProductById(int productId) {
        return String.format("http://192.168.1.67:8080/kinMel/product/%d", productId);
    }
    public static final String FETCH_PRODUCT_HOME_API_GRID = "http://192.168.1.67:8080/kinMel/products?sortBy=New";
    public static final String FETCH_PRODUCT_IMAGE_HOME_API = "http://192.168.1.67:8080/";

    public static final String KHALTI_PRIVATE_KEY="test_secret_key_c75b125dff0f4ee588edc9851a279b23";

}
