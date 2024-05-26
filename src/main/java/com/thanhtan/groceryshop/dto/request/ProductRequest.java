package com.thanhtan.groceryshop.dto.request;

import com.thanhtan.groceryshop.dto.response.BaseDTO;
import com.thanhtan.groceryshop.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    Long id;
    String name;
    String description;
    double price;
    double salePrice;
    int quantity;
    Long categoryId;
    ProductStatus productStatus;
    Set<Long> relatedProducts;

}