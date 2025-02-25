package com.ecommerce.project.service;

import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;


public interface CategoryService {


    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO CreateCategory(CategoryDTO categoryDTO);
    CategoryDTO DeleteCategory(Long id);
    CategoryDTO UpdateCategory(Long id,CategoryDTO categoryDTO);
}
