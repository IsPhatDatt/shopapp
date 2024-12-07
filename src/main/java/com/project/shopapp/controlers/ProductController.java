package com.project.shopapp.controlers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    //http://localhost:8088/api/v1/products?page=1&limit=10
    @GetMapping("")
    public ResponseEntity<String> getAllProducts(@RequestParam("page") int page,
                                                   @RequestParam("limit") int limit) {
        return ResponseEntity.ok(String.format("This is getAllProducts, page = %d and limit = %d", page, limit));
    }

    //http://localhost:8088/api/v1/products
    @PostMapping("")
    public ResponseEntity<?> insertProduct(@Valid @RequestBody ProductDTO product,
                                            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok("This is insertProduct " + product.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //http://localhost:8088/api/v1/products/7A
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id) {
        return ResponseEntity.ok(String.format("updateProduct with id = %s", id));
    }

    //http://localhost:8088/api/v1/products/7
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(String.format("deleteProduct with id = %s", id));
    }

}
