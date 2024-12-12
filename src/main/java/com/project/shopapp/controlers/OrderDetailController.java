package com.project.shopapp.controlers;

import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                               BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok("Create orderDetail successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderDetail(@PathVariable("id") int id) {
        return ResponseEntity.ok(String.format("getOrderDetail with ID: %d", id));
    }

    //Get the orderdetail list of an order
    @GetMapping("/order/{order_id}")
    public ResponseEntity<String> getOrderDetailsByOrderId(@PathVariable("order_id") int orderId) {
        return ResponseEntity.ok(String.format("getOrderDetails with order's ID: %d", orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable("id") int id,
                                               @Valid @RequestBody OrderDetailDTO newOrderDetailDTO,
                                               BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            //newOrderDetailDTO = newOrderDetailDTO.toString()
            return ResponseEntity.ok(String.format("Update orderDetail with ID: %d successfully - New data: %s", id, newOrderDetailDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable("id") int id) {
        //return ResponseEntity.ok(String.format("Delete orderDetail with ID: %d  successfully", id));
        //noContent() provider is HeadersBuilder<?> => noContent().build();
        return ResponseEntity.noContent().build();
    }
}
