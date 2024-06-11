package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.bean.request.CartItem;
import com.lagrandee.kinMel.bean.request.Order;
import com.lagrandee.kinMel.exception.NotInsertedException;
import com.lagrandee.kinMel.security.LoggedUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImplementation {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public String placeOrder(Order order, List<Integer> cartIds, HttpServletRequest request) {
        try {
            Integer buyerId = LoggedUser.findUser().getUserId();
            // Insert order into orders table
            if (order.getPaymentMethod().isEmpty() || order.getOrderTotal()==null || buyerId==null || cartIds.isEmpty()){
                throw new NotInsertedException("Required fields are empty");
            }
            String insertOrderSQL = "INSERT INTO orders (buyer_id, name, phone_number, address, payment_method, order_total) VALUES (?, ?, ?, ?, ?, ?)";
           jdbcTemplate.update(insertOrderSQL, buyerId, order.getName(), order.getPhoneNumber(), order.getAddress(), order.getPaymentMethod(), order.getOrderTotal());
            System.out.println("Buyer Id: "+buyerId);
            System.out.println("Order Name: "+order.getName());
            System.out.println("Order PhoneNumber: "+order.getPhoneNumber());
            System.out.println("Order Address: "+order.getAddress());
            System.out.println("Order Payment: "+order.getPaymentMethod());
            System.out.println("Order Payment: "+order.getOrderTotal());
            // Retrieve the generated order_id
//            int orderId = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Integer.class);
            int orderId = jdbcTemplate.queryForObject("SELECT IDENT_CURRENT('orders')", Integer.class);
            System.out.println(cartIds);
            // Fetch cart items from cart table

            String fetchCartItemsSQL = "SELECT cart_id, product_id, quantity, total FROM cart WHERE cart_id IN (:cartIds)";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("cartIds", cartIds);
            List<CartItem> cartItems = namedParameterJdbcTemplate.query(fetchCartItemsSQL, params, new CartItemMapper());
            cartItems.forEach(System.out::println);

            // Insert order items into order_items table
            String insertOrderItemSQL = "INSERT INTO order_items (order_id, product_id, quantity, total_price) VALUES (:orderId, :productId, :quantity, :totalPrice)";
            for (CartItem cartItem : cartItems) {
                MapSqlParameterSource itemParams = new MapSqlParameterSource()
                        .addValue("orderId", orderId)
                        .addValue("productId", cartItem.getProductId())
                        .addValue("quantity", cartItem.getQuantity())
                        .addValue("totalPrice", cartItem.getTotal());
                namedParameterJdbcTemplate.update(insertOrderItemSQL, itemParams);
            }

            // Delete cart items from cart table
            String deleteCartItemsSQL = "DELETE FROM cart WHERE cart_id IN (:cartIds)";
            namedParameterJdbcTemplate.update(deleteCartItemsSQL, params);

            return "Order Placed";
        } catch (DataAccessException e) {
            throw new NotInsertedException("Failed to insert");
        }
    }
    private static class CartItemMapper implements RowMapper<CartItem> {
        @Override
        public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            CartItem cartItem = new CartItem();
            cartItem.setCartId(rs.getInt("cart_id"));
            cartItem.setProductId(rs.getInt("product_id"));
            cartItem.setQuantity(rs.getInt("quantity"));
            cartItem.setTotal(rs.getLong("total"));
            return cartItem;
        }
    }

}
