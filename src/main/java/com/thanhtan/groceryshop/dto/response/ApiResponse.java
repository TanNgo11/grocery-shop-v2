package com.thanhtan.groceryshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class ApiResponse<T> {
    @Builder.Default
    private int code = 1000;
    String message;
    T result;
}
