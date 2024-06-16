package com.lagrandee.kinMel.bean.response;

import lombok.Data;

@Data
public class EntityDetail {
    private int totalSales;
    private int uniqueProducts;
    private int totalProducts;
    private int totalAdmin;
    private int totalCustomer;
    private int totalSeller;
    private int totalCategory;
    private int totalOrders;

}


//SELECT
//        (
//                SELECT SUM(order_items.total_price) from order_items
//) as sales,
//(
//Select Count(products.product_id) from products
//) as unique_Products,
//(
//SELECT SUM(products.stock_quantity) from products
//) as total_products,
//(
//Select  COUNT(DISTINCT users.user_id) from users  inner join roles_Assigned on
//users.user_id=roles_Assigned.user_id where roles_Assigned.role_id=2
//        ) as total_customer,
//(
//Select  COUNT(DISTINCT users.user_id) from users  inner join roles_Assigned on
//users.user_id=roles_Assigned.user_id where roles_Assigned.role_id=3
//        ) as total_seller,
//(
//Select COUNT(category.category_id) from category
//) as total_category,
//(
//Select COUNT(orders.order_id) from orders
//) as total_orders,
//(
//SELECT COUNT(users.user_id) from users inner join roles_Assigned on
//users.user_id=roles_Assigned.user_id where roles_Assigned.role_id=1
//        ) as total_admin


