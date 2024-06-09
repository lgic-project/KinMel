package com.lagrandee.kinMel.bean.request;

import lombok.Data;

@Data
public class CartItem {
    private int cartId;
    private int buyerId;
    private int productId;
    private int quantity;
    private String  addedAt;
    private long total;
}
