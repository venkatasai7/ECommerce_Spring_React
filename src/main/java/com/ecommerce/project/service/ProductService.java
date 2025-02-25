package com.ecommerce.project.service;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ProductService {

    ProductDTO addProduct(Long id, ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse SearchByCategory(Long categpryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse SearchProductByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO UpdateProduct(ProductDTO productDTO, Long id);

    ProductDTO DeleteProduct(Long id);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
