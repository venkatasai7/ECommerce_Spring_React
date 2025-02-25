package com.ecommerce.project.controller;
import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import com.ecommerce.project.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value="/public/categories",method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(
                            @RequestParam (name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                            @RequestParam (name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                            @RequestParam (name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
                            @RequestParam (name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder)
    {
         CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<> (categoryResponse,HttpStatus.OK);
    }

    @RequestMapping(value = "/public/categories",method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> CreateCategory(@Valid @RequestBody CategoryDTO categoryDTO)
    {
        CategoryDTO savedCategoryDTO = categoryService.CreateCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/admin/categories/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDTO> DeleteCategory(@PathVariable  Long id)
    {
            CategoryDTO categoryDTO = categoryService.DeleteCategory(id);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/public/categories/{id}",method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> UpdateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO)
    {
            CategoryDTO SavedCategoryDTO = categoryService.UpdateCategory(id,categoryDTO);
            return new ResponseEntity<>(SavedCategoryDTO, HttpStatus.OK);

    }


}
