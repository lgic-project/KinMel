package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.Repository.CategoryRepository;
import com.lagrandee.kinMel.bean.request.CategoryRequest;
import com.lagrandee.kinMel.bean.response.CategoryResponse;
import com.lagrandee.kinMel.exception.NotInsertedException;
import com.lagrandee.kinMel.helper.Image.ImageUtils;
import com.lagrandee.kinMel.helper.StaticPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImplementation {

private final CategoryRepository categoryRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryServiceImplementation(CategoryRepository categoryRepository, DataSource dataSource) {
        this.categoryRepository = categoryRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ResponseEntity<?> insertNewCategory(String categoryName, String categoryDescription,String categoryImage,String imageFormat) {

        String imageUploadPath= null;
        String savedImagePath=null;

        try {
            try {
                imageUploadPath = StaticPaths.getCategoryPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (categoryImage != null && !categoryImage.isEmpty()) {
                try {
                    savedImagePath = ImageUtils.saveDecodedImage(categoryImage, imageUploadPath,imageFormat,"category/");
                } catch (IOException e) {
                    throw new RuntimeException("Image cannot be saved");
                }
            }
            if (savedImagePath == null) {
                savedImagePath=StaticPaths.getProfileDefaultPath();
            }
            String categoryReturn = categoryRepository.insertNewCategory(categoryName, categoryDescription,savedImagePath);
            if (categoryReturn != null) {
                 KinMelCustomMessage customMessage=new KinMelCustomMessage(HttpStatus.OK.value(),"Category Added of Type : "+categoryReturn,System.currentTimeMillis());
                return new ResponseEntity<>(customMessage, HttpStatus.OK);
            } else {
                throw new NotInsertedException("Category insertion failed");
            }
        } catch (Exception e) {
            throw new NotInsertedException("Category insertion failed: "+ e.getMessage());
        }
    }
    public ResponseEntity<?> updateCategory(int categoryId, CategoryRequest categoryRequest) {
        try {
            String imageUploadPath = null;
            String savedImagePath = null;
            if(categoryRequest.getCategoryImage()!=null) {
                try {
                    try {
                        imageUploadPath = StaticPaths.getCategoryPath();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        savedImagePath = ImageUtils.saveDecodedImage(categoryRequest.getCategoryImage(), imageUploadPath, categoryRequest.getImageFormat(), "category/");
                    } catch (IOException e) {
                        throw new RuntimeException("Image cannot be saved");
                    }

                }
                catch (Exception e){
                    throw new NotInsertedException("Category update failed: "+ e.getMessage());
                }
            }

            String categoryReturn = categoryRepository.updateCategory(categoryId,categoryRequest.getCategoryName(), categoryRequest.getCategoryDescription(),savedImagePath);
            if (categoryReturn != null) {
                KinMelCustomMessage customMessage=new KinMelCustomMessage(HttpStatus.OK.value(),"Category Updated with new name : "+categoryReturn,System.currentTimeMillis());
                return new ResponseEntity<>(customMessage, HttpStatus.OK);
            } else {
                throw new NotInsertedException("Category update failed.Either category doesn't exist or failed to update");
            }
        } catch (Exception e) {
            throw new NotInsertedException("Category update failed: "+ e.getMessage());
        }
    }

//    @Transactional(readOnly = true)
    public  List<CategoryResponse> getAllCategories() {
        String sql = "{CALL GetAllCategories()}";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setCategory_id(rs.getInt("category_id"));
            categoryResponse.setCategory_name(rs.getString("category_name"));
            categoryResponse.setCategory_description(rs.getString("category_description"));
            categoryResponse.setImagePath(rs.getString("category_image"));
            return categoryResponse;
        });
    }

    public String requestNewCategory(String categoryName, String categoryDescription, String categoryImage, String imageFormat) {

        String imageUploadPath= null;
        String savedImagePath=null;

        try {
            try {
                imageUploadPath = StaticPaths.getCategoryPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (categoryImage != null && !categoryImage.isEmpty()) {
                try {
                    savedImagePath = ImageUtils.saveDecodedImage(categoryImage, imageUploadPath,imageFormat,"category/");
                } catch (IOException e) {
                    throw new RuntimeException("Image cannot be saved");
                }
            }
            if (savedImagePath == null) {
                savedImagePath=StaticPaths.getProfileDefaultPath();
            }

            String categoryRequestQuery = "INSERT INTO category_request(category_request_name,category_request_description,category_request_image_path) VALUES (?, ?, ?)";
            int insert = jdbcTemplate.update(categoryRequestQuery, categoryName, categoryDescription, savedImagePath);
//            String categoryRequestQuery = categoryRepository.insertNewCategory(categoryName, categoryDescription,savedImagePath);
            if (insert > 0) {
                return "Category Requested Sucessfully";
            } else {
                throw new NotInsertedException("Category insertion failed");
            }
        } catch (Exception e) {
            throw new NotInsertedException("Category insertion failed: "+ e.getMessage());
        }
    }

    public List<CategoryResponse> getRequestCategory() {
        String query="select * from category_request";
     return jdbcTemplate.query(query,(rs,rowNum)->{
            CategoryResponse categoryResponse=new CategoryResponse();
            categoryResponse.setCategory_id(rs.getInt("category_request_id"));
            categoryResponse.setCategory_name(rs.getString("category_request_name"));
            categoryResponse.setCategory_description(rs.getString("category_request_description"));
            categoryResponse.setImagePath(rs.getString("category_request_image_path"));
        return categoryResponse;
        });
    }

    public String approveCategoryRequest(Integer requestId) {
        try {
            String query = "select category_request_id,category_request_name, category_request_description, category_request_image_path from category_request where category_request_id=?";
            CategoryResponse categoryResponse = jdbcTemplate.queryForObject(query,(rs,rowName)->{
                CategoryResponse categoryResponse1 =new CategoryResponse();
                categoryResponse1.setCategory_id(rs.getInt("category_request_id"));
                categoryResponse1.setCategory_name(rs.getString("category_request_name"));
                categoryResponse1.setCategory_description(rs.getString("category_request_description"));
                categoryResponse1.setImagePath(rs.getString("category_request_image_path"));
                return categoryResponse1;
            }, requestId);
            String insertQuery="insert into category(category_name,category_description,category_image,category_created) values (?,?,?,?)";
            int insertSuccess = jdbcTemplate.update(insertQuery, categoryResponse.getCategory_name(), categoryResponse.getCategory_description(), categoryResponse.getImagePath(), LocalDateTime.now());

       String deleteQuery="delete from category_request where category_request_id=?";
       jdbcTemplate.update(deleteQuery,requestId);


        if (insertSuccess>0){
            return "Successfully approved";
        }
        return  "Failed to approve";
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        return "Failed to approve";
        }
    }
    public String rejectRequestForAddingCategory(Integer requestId) {
        try {
            String deleteQuery="delete from category_request where category_request_id=?";
            int update = jdbcTemplate.update(deleteQuery, requestId);
            if (update>0){
                return "Rejected the category";
            }
            return "Service Failure";
        }
        catch (Exception e){
            return "Server error";
        }

    }
}

