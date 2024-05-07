package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.bean.request.ProductRequest;
import com.lagrandee.kinMel.exception.NotInsertedException;
import com.lagrandee.kinMel.service.fileupload.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Types;
import java.util.List;

@Service
public class ProductServiceImplementation {

    private final FileUploadService fileUploadService;
    private final JdbcTemplate jdbcTemplate;

    public ProductServiceImplementation(FileUploadService fileUploadService, JdbcTemplate jdbcTemplate) {
        this.fileUploadService = fileUploadService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public String createNewProduct(ProductRequest productRequest, MultipartFile[] productImages) {
        if (productRequest.getPrice() == null || productRequest.getProductName()==null || productRequest.getCategoryId() ==null || productRequest.getStockQuantity()==null ) {
            throw new NotInsertedException("Fill all required fields");
        }

        // Upload files to the server
        List<String> imagePaths = fileUploadService.uploadFiles(productImages);

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
                .addValue("ProductImagePaths", String.join(",", imagePaths));

        jdbcCall.execute(parameters);
        return "Created Successfully:- "+productRequest.getProductName();



    }
}
