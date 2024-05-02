package com.lagrandee.kinMel.service.implementation;


import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.Repository.CategoryRepository;
import com.lagrandee.kinMel.bean.request.CategoryRequest;
import com.lagrandee.kinMel.bean.response.CategoryResponse;
import com.lagrandee.kinMel.exception.NotInsertedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
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

    public ResponseEntity<?> insertNewCategory(String categoryName, String categoryDescription) {
        try {
            String categoryReturn = categoryRepository.insertNewCategory(categoryName, categoryDescription);
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
            String categoryReturn = categoryRepository.updateCategory(categoryId,categoryRequest.getCategoryName(), categoryRequest.getCategoryDescription());
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
            return categoryResponse;
        });
    }
    }

