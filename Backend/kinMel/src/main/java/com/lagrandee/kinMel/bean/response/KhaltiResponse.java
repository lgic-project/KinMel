package com.lagrandee.kinMel.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KhaltiResponse {
    @JsonProperty("pidx")
    String pidx;
    @JsonProperty("payment_url")
    String paymentURL;
    @JsonProperty("user_fee")
    Integer userfee;
    @JsonProperty("expires_at")
    String expirery;
    @JsonProperty("expires_in")
    Long expirySecond;
}
