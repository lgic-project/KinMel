package com.example.kinmel.StaticFiles;

public class ApiStatic {
    public static final String USER_REGISTRATION_API = "http://192.168.1.67:8080/kinMel/users/register";
    public static final String REGENERATE_OTP_API = "http://192.168.1.67:8080/kinMel/regenerate-otp";
    public static final String VERIFY_ACCOUNT_API = "http://192.168.1.67:8080/kinMel/verify-account";
    public static final String LOGIN_ACCOUNT_API = "http://192.168.1.67:8080/kinMel/login";
    public static final String FETCH_PRODUCT_HOME_API = "http://192.168.1.67:8080/kinMel/products";
    public static String getProductById(int productId) {
        return String.format("http://192.168.1.67:8080/kinMel/product/%d", productId);
    }
    public static final String FETCH_PRODUCT_HOME_API_GRID = "http://192.168.1.67:8080/kinMel/products?sortBy=New";
    public static final String FETCH_PRODUCT_IMAGE_HOME_API = "http://192.168.1.67:8080/";

}
