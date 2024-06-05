package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.bean.response.CartResponse;
import com.lagrandee.kinMel.bean.response.CategoryResponse;
import com.lagrandee.kinMel.security.JwtUtils;
import com.lagrandee.kinMel.security.LoggedUser;
import com.lagrandee.kinMel.security.filter.AuthTokenFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    public String deleteCart(List<Integer> cartIds) {
        SimpleJdbcCall deleteCartByIdProc = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DeleteCartById")
                .declareParameters(
                        new SqlParameter("cartId", Types.INTEGER),
                        new SqlOutParameter("result", Types.NVARCHAR)
                        // Remove the '@' prefix
                );

        if (cartIds == null || cartIds.isEmpty()) {
            return "No cart items provided for deletion.";
        }

        int successCount = 0;
        int failCount = 0;
        StringBuilder resultBuilder = new StringBuilder();

        for (Integer cartId : cartIds) {
            try {
                MapSqlParameterSource paramMap = new MapSqlParameterSource()
                        .addValue("cartId", cartId);  // Match the name without '@'

                Map<String, Object> result = deleteCartByIdProc.execute(paramMap);
                String message = (String) result.get("result");
                if (message.contains("successfully")) {
                    successCount++;
                    System.out.println("  - Counted as success");
                }else {
                    failCount++;
                    resultBuilder.append("Failed to delete cart item ").append(cartId).append(": ")
                            .append(message).append("\n");
                }
            } catch (Exception e) {
                failCount++;
                resultBuilder.append("Error deleting cart item ").append(cartId).append(": ")
                        .append(e.getMessage()).append("\n");
            }
        }

        resultBuilder.insert(0, String.format("Successfully deleted %d cart item(s). ", successCount));
        if (failCount > 0) {
            resultBuilder.append(String.format("Failed to delete %d cart item(s).", failCount));
        }

        return resultBuilder.toString();
    }

    public List<CartResponse> getAllCart(HttpServletRequest request) {
        Integer buyerId = LoggedUser.findUser().getUserId();
        String sql = "{CALL GetAllCartByUserId(:buyerId)}";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("buyerId", buyerId);

        return new NamedParameterJdbcTemplate(jdbcTemplate).query(sql, paramSource, (rs, rowNum) -> {
            CartResponse cartResponse = new CartResponse();
            cartResponse.setCartId(rs.getInt("cart_id"));
            cartResponse.setPrice(rs.getInt("price"));
            cartResponse.setDiscountedPrice(rs.getInt("discounted_price"));
            cartResponse.setQuantity(rs.getInt("quantity"));
            cartResponse.setTotal(rs.getInt("total"));
            cartResponse.setProductName(rs.getString("product_name"));
            cartResponse.setProductImagePath(rs.getString("product_image_path"));
            return cartResponse;
        });
    }
//@Transactional
//public String deleteCart(List<Integer> cartIds) {
//    if (cartIds == null || cartIds.isEmpty()) {
//        return "No cart items provided for deletion.";
//    }
//
//    String sql = "DELETE FROM [dbo].[cart] WHERE [cart_id] IN (:cartIds)";
//    Map<String, Object> paramMap = Collections.singletonMap("cartIds", cartIds);
//
//    int deletedCount = namedParameterJdbcTemplate.update(sql, paramMap);
//
//    if (deletedCount != cartIds.size()) {
//        throw new RuntimeException("Not all items were found. Transaction rolled back.");
//    }
//
//    return "Successfully deleted all " + deletedCount + " cart item(s).";
//}
}
