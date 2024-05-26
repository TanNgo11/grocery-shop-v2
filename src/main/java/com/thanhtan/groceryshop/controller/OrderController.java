package com.thanhtan.groceryshop.controller;

import com.querydsl.core.Tuple;
import com.thanhtan.groceryshop.dto.request.OrderRequest;
import com.thanhtan.groceryshop.dto.request.UpdateOrderRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.MonthlySalesResponse;
import com.thanhtan.groceryshop.dto.response.OrderResponse;
import com.thanhtan.groceryshop.enums.OrderStatus;
import com.thanhtan.groceryshop.service.IOrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.thanhtan.groceryshop.constant.PathConstant.API_V1_ORDERS;

@RestController
@RequestMapping(API_V1_ORDERS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    IOrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(orderRequest))
                .build();
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<OrderResponse>> getAllOrder(@PageableDefault(size = 10) Pageable pageable, @RequestParam(required = false) String searchTerm) {
        return ApiResponse.<Page<OrderResponse>>builder()
                .result(orderService.getAllOrderWithoutOrderItems(pageable, searchTerm))
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrder(orderId))
                .build();
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<OrderResponse> updateOrder(@RequestBody @Valid UpdateOrderRequest updateOrderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrder(updateOrderRequest))
                .build();
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<OrderResponse> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrderStatus(orderId, status))
                .build();
    }

    @GetMapping("/monthly-sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<MonthlySalesResponse>> getMonthlySales() {
        return ApiResponse.<List<MonthlySalesResponse>>builder()
                .result(orderService.getMonthlySales())
                .build();
    }

    @GetMapping("/daily-sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> findNumberOfOrderDaily() {
        return ApiResponse.<Long>builder()
                .result(orderService.findNumberOfOrderDaily())
                .build();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<OrderResponse>> findAllOrdersByUser() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.findAllOrdersByUser())
                .build();
    }

}
