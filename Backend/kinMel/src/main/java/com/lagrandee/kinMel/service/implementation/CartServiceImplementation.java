package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.security.JwtUtils;
import com.lagrandee.kinMel.security.filter.AuthTokenFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation {
    private final JdbcTemplate jdbcTemplate;
    private final JwtUtils jwtTokenProvider;

    private final AuthTokenFilter authTokenFilter;

    public String createNewCart(int productId, HttpServletRequest request) {
        try {
            String token = authTokenFilter.parseJwt(request);
            Integer userId = jwtTokenProvider.getUserIdFromJWT(token);

            Map<String, Object> inputs = new HashMap<>();
            inputs.put("buyer_id", userId);
            inputs.put("product_id", productId);

            SimpleJdbcCall insertNewCartProcedure = new SimpleJdbcCall(jdbcTemplate.getDataSource())
                    .withProcedureName("InsertNewCart");

            insertNewCartProcedure.execute(inputs);
            return "Added to cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String updateCart(int cartId,String quantityChange) {
        try {

            Map<String, Object> inputs = new HashMap<>();
            inputs.put("cart_id", cartId);
            inputs.put("quantity_change", quantityChange);

            SimpleJdbcCall updateCartQuantity = new SimpleJdbcCall(jdbcTemplate.getDataSource())
                    .withProcedureName("UpdateCartQuantity");

            updateCartQuantity.execute(inputs);
            return "Update Successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }

    }
}
