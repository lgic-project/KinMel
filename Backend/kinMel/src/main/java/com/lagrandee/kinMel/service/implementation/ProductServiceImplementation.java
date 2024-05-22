package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.bean.request.ProductRequest;
import com.lagrandee.kinMel.bean.response.ProductResponse;
import com.lagrandee.kinMel.exception.NotInsertedException;
import com.lagrandee.kinMel.exception.UserNotVerified;
import com.lagrandee.kinMel.security.JwtUtils;
import com.lagrandee.kinMel.security.filter.AuthTokenFilter;
import com.lagrandee.kinMel.service.fileupload.FileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Types;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation {

    private final JdbcTemplate jdbcTemplate;
    private final JwtUtils jwtTokenProvider;

    private final AuthTokenFilter authTokenFilter;
    private  final UserServiceImplementation userServiceImplementation;

//    public ProductServiceImplementation(JdbcTemplate jdbcTemplate, JwtUtils jwtTokenProvider, AuthTokenFilter authTokenFilter, UserServiceImplementation userServiceImplementation) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.authTokenFilter = authTokenFilter;
//        this.userServiceImplementation = userServiceImplementation;
//    }

    public String createNewProduct(ProductRequest productRequest, MultipartFile[] productImages, HttpServletRequest request) {
        String token = authTokenFilter.parseJwt(request);
        Integer sellerId = jwtTokenProvider.getUserIdFromJWT(token);
        if (productRequest.getPrice() == null || productRequest.getProductName() == null || productRequest.getCategoryId() == null || productRequest.getStockQuantity() == null) {
            throw new NotInsertedException("Fill all required fields");
        }
        Integer activeValue = userServiceImplementation.isUserActive(sellerId);
        if (activeValue == null || activeValue != 1) {
            throw new UserNotVerified("User is not verified");
        }

        // Upload files to the server
        List<String> imagePaths = null;
        try {
            imagePaths = FileUploadService.saveMultipartImages(productImages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("InsertProduct")
                .declareParameters(
                        new SqlParameter("ProductName", Types.VARCHAR),
                        new SqlParameter("ProductDescription", Types.VARCHAR),
                        new SqlParameter("CategoryId", Types.INTEGER),
                        new SqlParameter("Brand", Types.VARCHAR),
                        new SqlParameter("Price", Types.INTEGER),
                        new SqlParameter("DiscountedPrice", Types.INTEGER),
                        new SqlParameter("StockQuantity", Types.INTEGER),
                        new SqlParameter("ProductStatus", Types.INTEGER),
                        new SqlParameter("Featured", Types.INTEGER),
                        new SqlParameter("SellerId", Types.INTEGER),
                        new SqlParameter("ProductImagePaths", Types.VARCHAR)
                );

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ProductName", productRequest.getProductName())
                .addValue("ProductDescription", productRequest.getProductDescription())
                .addValue("CategoryId", productRequest.getCategoryId())
                .addValue("Brand", productRequest.getBrand())
                .addValue("Price", productRequest.getPrice())
                .addValue("DiscountedPrice", productRequest.getDiscountedPrice())
                .addValue("StockQuantity", productRequest.getStockQuantity())
                .addValue("ProductStatus", productRequest.getProductStatus())
                .addValue("Featured", productRequest.getFeatured())
                .addValue("SellerId", sellerId)
                .addValue("ProductImagePaths", String.join(",", imagePaths));

        jdbcCall.execute(parameters);
        return "Created Successfully:- " + productRequest.getProductName();
    }


    public List<ProductResponse> getAllProduct(String productName,String sortBy, String categoryName,Long maxPrice, String brandName) {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_SortProducts")
                .declareParameters(
                        new SqlParameter("ProductName", Types.VARCHAR),
                        new SqlParameter("BrandName", Types.VARCHAR),
                        new SqlParameter("SortBy", Types.VARCHAR),
                        new SqlParameter("CategoryName", Types.VARCHAR),
                        new SqlParameter("MaxPrice", Types.BIGINT)
                );

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ProductName", productName)
                .addValue("BrandName", brandName)
                .addValue("SortBy", sortBy)
                .addValue("CategoryName", categoryName)
                .addValue("MaxPrice", maxPrice);


        Map<String, Object> resultMap = jdbcCall.execute(parameters);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get("#result-set-1");
        List<ProductResponse> products = new ArrayList<>();
        for (Map<String, Object> row : resultList) {
            ProductResponse product = new ProductResponse();
            product.setProductId((Integer) row.get("product_id"));
            product.setProductName((String) row.get("product_name"));
            product.setProductDescription((String) row.get("product_description"));
            product.setCategoryName((String) row.get("category_name"));
            product.setBrand((String) row.get("brand"));
            product.setPrice((Long) row.get("price"));
            product.setDiscountedPrice((Long) row.get("discounted_price"));
            product.setStockQuantity((Integer) row.get("stock_quantity"));
            product.setSellerId((Integer) row.get("seller_id"));
            product.setProductStatus((Integer) row.get("product_status"));
            product.setFeatured((Integer) row.get("featured"));
            product.setCreatedAt((Date) row.get("created_at"));
            product.setUpdatedAt((Date) row.get("updated_at"));
            String imagePaths = (String) row.get("product_images");
            List<String> imagePathList = Arrays.asList(imagePaths.split(", "));
            product.setProductImages(imagePathList);
            products.add(product);
        }
        return products;
    }
    }

