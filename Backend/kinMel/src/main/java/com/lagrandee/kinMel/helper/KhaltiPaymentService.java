package com.lagrandee.kinMel.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lagrandee.kinMel.bean.response.KhaltiResponse;
import com.lagrandee.kinMel.exception.KhaltiPaymentException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class KhaltiPaymentService {

    private String khaltiTestApiUrl = "https://a.khalti.com/api/v2/epayment/initiate/";
    private String khaltiTestApiKey = "live_secret_key_68791341fdd94846a146f0457ff7b455";

    public KhaltiResponse initiatePayment(KhaltiPaymentRequest paymentRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "key " + khaltiTestApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<KhaltiPaymentRequest> requestEntity = new HttpEntity<>(paymentRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                khaltiTestApiUrl ,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(response.getBody(), KhaltiResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse Khalti response", e);
            }
        } else {
            // Check for specific error cases
            String errorBody = response.getBody();
            if (errorBody.contains("This field may not be blank.")) {
                throw new KhaltiPaymentException("One or more required fields are missing");
            } else if (errorBody.contains("Enter a valid URL.")) {
                throw new KhaltiPaymentException("Invalid URL provided");
            } else if (errorBody.contains("Amount should be greater than Rs. 1, that is 100 paisa.")) {
                throw new KhaltiPaymentException("Amount should be greater than Rs. 10");
            } else if (errorBody.contains("A valid integer is required.")) {
                throw new KhaltiPaymentException("Invalid amount provided");
            } else if (errorBody.contains("Amount Breakdown mismatch.")) {
                throw new KhaltiPaymentException("Amount breakdown does not match the total amount");
            } else {
                throw new KhaltiPaymentException("Khalti payment initiation failed. Status code: " + response.getStatusCode());
            }
        }
    }
}