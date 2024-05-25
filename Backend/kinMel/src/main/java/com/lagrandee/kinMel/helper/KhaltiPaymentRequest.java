package com.lagrandee.kinMel.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class KhaltiPaymentRequest {

    private String return_url; // Mandatory
    private String website_url;
    private int amount;
    private String purchase_order_id;
    private String purchase_order_name;
    private CustomerInfo customer_info;

    // Getters and Setters (omitted for brevity)

   @Data
   @AllArgsConstructor
    public static class CustomerInfo {
        private String name;
        private String email;
       private String phone;

        // Getters and Setters (omitted for brevity)
    }
}

