package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.bean.request.RatingRequest;
import com.lagrandee.kinMel.security.LoggedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RatingServiceImplementation {
    private final JdbcTemplate jdbcTemplate;

    public String addRating(RatingRequest ratingRequest){

        Integer buyerId= LoggedUser.findUser().getUserId();

        boolean hasOrdered = checkIfUserOrderedProduct(buyerId, ratingRequest.getProductId());
        if (!hasOrdered) {
            return "Order this product first to give rating";
        }

        Integer existingRatingId = checkExistingRating(buyerId, ratingRequest.getProductId());
        if (existingRatingId != null) {
            // Update existing rating
            updateRating(existingRatingId, ratingRequest.getRating());
            return "Rating updated successfully";
        } else {
            // Add new rating
            addNewRating(ratingRequest,buyerId);
            return "Rating added successfully";
        }
    }

    private void addNewRating(RatingRequest request,Integer buyerId) {
        String sql = "INSERT INTO product_rating (product_id, rating, buyer_id) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, request.getProductId(), request.getRating(), buyerId);
    }

    private void updateRating(Integer ratingId, BigDecimal rating) {
        String sql = "UPDATE product_rating SET rating = ?, created_at = GETDATE() " +
                "WHERE ratingId = ?";
        jdbcTemplate.update(sql, rating, ratingId);
    }

    private Integer checkExistingRating(int buyerId, int productId) {
        String sql = "SELECT ratingId FROM product_rating " +
                "WHERE buyer_id = ? AND product_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, buyerId, productId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private boolean checkIfUserOrderedProduct(int buyerId, int productId) {
        String sql = "SELECT COUNT(*) FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "WHERE o.buyer_id = ? AND oi.product_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, buyerId, productId);
        return count > 0;
    }

}
