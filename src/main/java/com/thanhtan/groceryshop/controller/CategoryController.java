package com.thanhtan.groceryshop.controller;

import com.thanhtan.groceryshop.dto.request.CategoryRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.CategoryResponse;
import com.thanhtan.groceryshop.service.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    ICategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest category) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(category))
                .build();
    }
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.findAll())
                .build();
    }


    @GetMapping("/{categoryName}/products/count")
    public ApiResponse<Long> countProductByCategory(@PathVariable String categoryName) {
        return ApiResponse.<Long>builder()
                .result(categoryService.countProductByCategory(categoryName))
                .build();
    }
}
