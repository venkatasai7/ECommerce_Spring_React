package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class ProductController {

    @Autowired
    ProductService productService;



    @PostMapping("/admin/categories/{Id}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long Id) {
        ProductDTO savedProductDTO = productService. addProduct(Id,productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue =  AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder


    ) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @GetMapping("/public/categories/{categpryId}/product/")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categpryId,   @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue =  AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.SearchByCategory(categpryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword, @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue =  AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder){
        ProductResponse productResponse = productService.SearchProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }



    @PutMapping("/admin/products/{Id}")
    public ResponseEntity<ProductDTO> UpdateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long Id) {
        ProductDTO UpdatedPorductDTO= productService.UpdateProduct(productDTO,Id);
        return new ResponseEntity<>(UpdatedPorductDTO, HttpStatus.OK);
    }


    @DeleteMapping("/admin/products/{Id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long Id) {

        ProductDTO DeletedProductDTO = productService.DeleteProduct(Id);
        return new ResponseEntity<>(DeletedProductDTO, HttpStatus.OK);

    }


    @PutMapping("/products/{Id}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long Id, @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct  = productService.updateProductImage(Id,image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }

}

