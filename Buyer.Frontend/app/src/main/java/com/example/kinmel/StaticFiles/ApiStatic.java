package com.example.kinmel.StaticFiles;

import java.util.List;
import java.util.stream.Collectors;

public class ApiStatic {
    public static final String USER_REGISTRATION_API = "http://10.0.2.2:8080/kinMel/users/register";
    public static final String REGENERATE_OTP_API = "http://10.0.2.2:8080/kinMel/regenerate-otp";
    public static final String VERIFY_ACCOUNT_API = "http://10.0.2.2:8080/kinMel/verify-account";
    public static final String LOGIN_ACCOUNT_API = "http://10.0.2.2:8080/kinMel/login";
    public static final String FETCH_PRODUCT_HOME_API = "http://10.0.2.2:8080/kinMel/products";
    public static final String FETCH_USER_DETAIL = "http://10.0.2.2:8080/kinMel/user";
    public static final String FETCH_USER_CART = "http://10.0.2.2:8080/kinMel/carts";
    public static final String UPDATE_USER_PROFILE = "http://10.0.2.2:8080/kinMel/users";
    public static final String CHANGE_PASSWORD_URL = "http://10.0.2.2:8080/kinMel/users/changepassword";
    public static final String FETCH_ORDER_HISTORY = "http://10.0.2.2:8080/kinMel/order";
    public static String getProductById(int productId) {
        return String.format("http://10.0.2.2:8080/kinMel/product/%d", productId);
    }

    public static final String FETCH_SEARCH_API(String query) {
        return String.format("http://10.0.2.2:8080/kinMel/products?productName=%s", query);
    }

    public static final String FETCH_PRODUCT_HOME_API_GRID = "http://10.0.2.2:8080/kinMel/products?sortBy=New";
    public static final String FETCH_PRODUCT_IMAGE_HOME_API = "http://10.0.2.2:8080/";
    public static String ADD_TO_CART_API(int productId) {
        return String.format("http://10.0.2.2:8080/kinMel/carts?productId=%d", productId);
    }

    public static String DELETE_ITEMS_FROM_CART(List<Integer> cartIds) {
        String cartIdsParam = cartIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return String.format("http://10.0.2.2:8080/kinMel/carts?cartId=%s", cartIdsParam);
    }

    public static String PLACE_ORDER_API (List<Integer> cartIds){
        String cartIdsParam = cartIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return String.format("http://10.0.2.2:8080/kinMel/order?cartIds=%s",cartIdsParam);
    }

    public static final String CHANGE_QUANTITY_API (int cartId, String changeValue) {
        return String.format("http://10.0.2.2:8080/kinMel/carts?cartId=%d&quantityChange=%s", cartId, changeValue);
    }


    public static String FETCH_BRAND_WITH_PRICE_API(String query, String maxPrice) {
        return String.format("http:////10.0.2.2:8080kinMel/products?brandName=%s&maxPrice=%d", query, maxPrice);
    }

    public static String FETCH_BRAND_API(String query) {
        return String.format("http:////10.0.2.2:8080kinMel/products?brandName=%s", query);

    }

    public static String FETCH_PRODUCT_NAME_WITH_PRICE_API(String query, String maxPrice) {
        return String.format("http:////10.0.2.2:8080kinMel/products?productName=%s&maxPrice=%s", query, maxPrice);
    }

    public static String FETCH_PRODUCT_NAME_API(String query) {
        return String.format("http:////10.0.2.2:8080kinMel/products?productName=%s", query);
    }
}
