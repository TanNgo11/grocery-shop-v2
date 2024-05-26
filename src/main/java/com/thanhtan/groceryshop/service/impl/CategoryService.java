package com.thanhtan.groceryshop.service.impl;

import com.thanhtan.groceryshop.dto.request.CategoryRequest;
import com.thanhtan.groceryshop.dto.response.CategoryResponse;
import com.thanhtan.groceryshop.entity.Category;
import com.thanhtan.groceryshop.mapper.CategoryMapper;
import com.thanhtan.groceryshop.repository.CategoryRepository;
import com.thanhtan.groceryshop.repository.ProductRepository;
import com.thanhtan.groceryshop.service.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {
    CategoryRepository categoryRepository;

    CategoryMapper categoryMapper;

    ProductRepository productRepository;

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> listCategory = categoryRepository.findAll();

        return listCategory.stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public long countProductByCategory(String categoryName) {
        if (categoryName.equals("All Products")) {
            return productRepository.count();
        }

        return categoryRepository.countProductByCategory(categoryName);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest category) {

        Category savedCategory = categoryRepository.save(categoryMapper.toCategory(category));


        return categoryMapper.toCategoryResponse(savedCategory);
    }
}
