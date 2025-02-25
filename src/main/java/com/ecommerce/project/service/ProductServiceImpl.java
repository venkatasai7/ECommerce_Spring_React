package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIExceptions;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;

    @Value("${project.images}")
    private String path;




    @Override
    public ProductDTO addProduct(Long id, ProductDTO productDTO) {
        Category category = categoryRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Category", "id", id));

        boolean ifProductPresent = false;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName()))
            {
                ifProductPresent = true;
                break;
            }

        }


        if(ifProductPresent)
                throw new APIExceptions("Product already exists");

        Product product = modelMapper.map(productDTO, Product.class);



        product.setCategory(category);

        double specailPrice = product.getPrice() - ((product.getDiscount() * 0.01)*product.getPrice());
        product.setProductImage("default.img");
        product.setSpecialPrice(specailPrice);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder =
                sortOrder.equalsIgnoreCase("ASC")?
                    Sort.by(sortBy).ascending() :
                        Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products =pageProducts.getContent();



        List<ProductDTO> productDTOS = products.stream().
                map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse SearchByCategory(Long categpryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categpryId).
                orElseThrow(()->new ResourceNotFoundException("Category", "id", categpryId));

        Sort sortByAndOrder =
                sortOrder.equalsIgnoreCase("ASC")?
                        Sort.by(sortBy).ascending() :
                        Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);

        List<Product> products =pageProducts.getContent();


        if(products.isEmpty())
            throw new APIExceptions("No products found");

        List<ProductDTO> productDTOS = products.stream().
                map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }



    @Override
    public ProductResponse SearchProductByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder =
                sortOrder.equalsIgnoreCase("ASC")?
                        Sort.by(sortBy).ascending() :
                        Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%',pageDetails);

        List<Product> products =pageProducts.getContent();




        if(products.isEmpty())
            throw new APIExceptions("No products found");

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO UpdateProduct(ProductDTO productDTO, Long id) {
        Product DBproduct = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product","productId",id));
        Product product = modelMapper.map(productDTO, Product.class);
        DBproduct.setCategory(product.getCategory());
        DBproduct.setPrice(product.getPrice());
        DBproduct.setProductImage(product.getProductImage());
        DBproduct.setSpecialPrice(product.getSpecialPrice());
        DBproduct.setProductName(product.getProductName());
        DBproduct.setDescription(product.getDescription());
        DBproduct.setDiscount(product.getDiscount());
        DBproduct.setQuantity(product.getQuantity());




        return modelMapper.map(productRepository.save(DBproduct), ProductDTO.class);

    }

    @Override
    public ProductDTO DeleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Product", "id", id)
        );

        productRepository.delete(product);

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);

        productFromDb.setProductImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }



}
