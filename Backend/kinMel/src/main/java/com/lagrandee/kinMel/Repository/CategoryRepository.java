package com.lagrandee.kinMel.Repository;

import com.lagrandee.kinMel.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Procedure(name = "InsertNewCategory", outputParameterName = "CategoryReturn")
    String insertNewCategory(@Param("CategoryName") String categoryName, @Param("CategoryDescription") String categoryDescription ,@Param("CategoryImage") String categoryImage);

    @Procedure(name = "UpdateCategory", outputParameterName = "CategoryReturn")
    String updateCategory(@Param("CategoryId") int categoryId,@Param("CategoryName") String categoryName, @Param("CategoryDescription") String categoryDescription,@Param("CategoryImage") String categoryImage);

}