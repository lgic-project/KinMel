package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.bean.response.DashboardCategoryResponse;
import com.lagrandee.kinMel.bean.response.EntityDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailsServiceImplementation {
    private final JdbcTemplate jdbcTemplate;

    public EntityDetail fetchEntityDetails() {
        StringBuilder query=new StringBuilder();
        query.append(" SELECT ").append("  (\n" +
                "                SELECT SUM(order_items.total_price) from order_items\n" +
                ") as sales, ").append(" (\n" +
                "Select Count(products.product_id) from products\n" +
                ") as unique_Products, ").append(" (\n" +
                "SELECT SUM(products.stock_quantity) from products\n" +
                ") as total_products, ").append(" (\n" +
                "Select  COUNT(DISTINCT users.user_id) from users  inner join roles_Assigned on\n" +
                "users.user_id=roles_Assigned.user_id where roles_Assigned.role_id=2\n" +
                "        ) as total_customer, ").append(" (\n" +
                "Select  COUNT(DISTINCT users.user_id) from users  inner join roles_Assigned on\n" +
                "users.user_id=roles_Assigned.user_id where roles_Assigned.role_id=3\n" +
                "        ) as total_seller, ").append(" (\n" +
                "Select COUNT(category.category_id) from category\n" +
                ") as total_category, ").append(" (\n" +
                "Select COUNT(orders.order_id) from orders\n" +
                ") as total_orders, ").append("(\n" +
                "SELECT COUNT(users.user_id) from users inner join roles_Assigned on\n" +
                "users.user_id=roles_Assigned.user_id where roles_Assigned.role_id=1\n" +
                "        ) as total_admin ");
      return  jdbcTemplate.queryForObject(query.toString(),(rs,rowName)->{
            EntityDetail entityDetail=new EntityDetail();
            entityDetail.setTotalSales(rs.getInt("sales"));
          entityDetail.setUniqueProducts(rs.getInt("unique_products"));
          entityDetail.setTotalProducts(rs.getInt("total_products"));
          entityDetail.setTotalCustomer(rs.getInt("total_customer"));
          entityDetail.setTotalSeller(rs.getInt("total_seller"));
          entityDetail.setTotalCategory(rs.getInt("total_category"));
          entityDetail.setTotalOrders(rs.getInt("total_orders"));
          entityDetail.setTotalAdmin(rs.getInt("total_admin"));
          return entityDetail;
        });
    }

    public List<DashboardCategoryResponse> fetchEachCategoryDetails() {
            String query= """
                    SELECT\s
                        c.category_name,
                        COUNT(DISTINCT p.product_id) AS NumberOfProducts,
                        COUNT(od.order_item_id) AS TotalOrders
                    FROM\s
                        category c
                        inner JOIN Products p ON c.category_id = p.category_id
                        LEFT JOIN order_items od ON p.product_id = od.product_id
                    GROUP BY\s
                        c.category_name
                    ORDER BY\s
                        c.category_name;
                                        
                    """;
            return jdbcTemplate.query(query,(rs,rowName)->{
               DashboardCategoryResponse categoryResponse=new DashboardCategoryResponse();
               categoryResponse.setCategoryName(rs.getString("category_name"));
               categoryResponse.setNoOfProducts(rs.getInt("NumberOfProducts"));
               categoryResponse.setTotalOrdersInEachCategory(rs.getInt("TotalOrders"));
               return categoryResponse;

            });
    }
}
