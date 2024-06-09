package com.lagrandee.kinMel.bean.request;

import lombok.Data;

@Data
public class Order {
private String name;
private String phoneNumber;
private String address;
private String paymentMethod;
private Long orderTotal;
}
