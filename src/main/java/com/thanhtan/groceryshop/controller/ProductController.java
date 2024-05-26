package com.thanhtan.groceryshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhtan.groceryshop.dto.request.ProductRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.ProductResponse;
import com.thanhtan.groceryshop.dto.response.ProductSalesResponse;
import com.thanhtan.groceryshop.dto.response.SalesCategory;
import com.thanhtan.groceryshop.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.thanhtan.groceryshop.constant.PathConstant.API_V1_PRODUCTS;


@RestController
@RequestMapping(API_V1_PRODUCTS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    IProductService productService;


    @GetMapping
    public ApiResponse<List<ProductResponse>> getProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findAll())
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> createProduct(@RequestPart("product") String productStr,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest productRequest = objectMapper.readValue(productStr, ProductRequest.class);

        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productRequest, file))
                .build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> updateProduct(@RequestPart("product") String productStr,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest productRequest = objectMapper.readValue(productStr, ProductRequest.class);

        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(productRequest, file))
                .build();
    }

    @GetMapping("/sales")
    public ApiResponse<List<ProductResponse>> getSalesProduct() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findSalesProduct())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.findById(id))
                .build();
    }

    @GetMapping("/detail/{slug}")
    public ApiResponse<ProductResponse> getProductBySlug(@PathVariable String slug) {

        return ApiResponse.<ProductResponse>builder()
                .result(productService.findBySlug(slug))
                .build();
    }

    @GetMapping("/category/{categoryName}")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(@PathVariable String categoryName, @RequestParam(required = false) Integer limit) {
        if (limit != null) {
            return ApiResponse.<List<ProductResponse>>builder()
                    .result(productService.find5ProductsByCategory(categoryName))
                    .build();
        }

        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findByCategory(categoryName))
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<List<ProductResponse>> getProductsByIds(@RequestParam List<Long> ids) {
        List<ProductResponse> products = ids.stream()
                .map(productService::findById)
                .toList();
        return ApiResponse.<List<ProductResponse>>builder()
                .result(products)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> searchProducts(@RequestParam String q) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findProductsByQueryString(q))
                .build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteProducts(@RequestParam Long[] ids) {
        productService.deleteProductsById(ids);
        return ApiResponse.<String>builder()
                .message("Delete successfully")
                .build();
    }


    @GetMapping("/most-sold")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ProductSalesResponse>> getMostSoldProduct() {
        return ApiResponse.<List<ProductSalesResponse>>builder()
                .result(productService.findMostSoldProduct())
                .build();
    }

    @GetMapping("/sales-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<SalesCategory>> getSalesCategory(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate) {
        return ApiResponse.<List<SalesCategory>>builder()
                .result(productService.findSalesCategory(startDate, endDate))
                .build();
    }


//    @PutMapping("/status")
//    public ApiResponse<String> updateProductStatus(@RequestParam Long[] ids) {
//        productService.updateProductStatus(ids);
//        return ApiResponse.<String>builder()
//                .build();
//    }


}
