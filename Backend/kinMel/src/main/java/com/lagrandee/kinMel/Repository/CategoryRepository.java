package com.lagrandee.kinMel.Repository;

import com.lagrandee.kinMel.bean.response.CategoryResponse;
import com.lagrandee.kinMel.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Procedure(name = "InsertNewCategory", outputParameterName = "CategoryReturn")
    String insertNewCategory(@Param("CategoryName") String categoryName, @Param("CategoryDescription") String categoryDescription);


    @Procedure(name = "UpdateCategory", outputParameterName = "CategoryReturn")
    String updateCategory(@Param("CategoryId") int categoryId,@Param("CategoryName") String categoryName, @Param("CategoryDescription") String categoryDescription);

}