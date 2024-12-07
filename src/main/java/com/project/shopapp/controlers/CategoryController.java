package com.project.shopapp.controlers;

import com.project.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
//@Validated
public class CategoryController {

    //http://localhost:8088/api/v1/categories?page=1&limit=10
    @GetMapping("")
    public ResponseEntity<String> getAllCategories(@RequestParam("page") int page,
                                                   @RequestParam("limit") int limit) {
        return ResponseEntity.ok(String.format("This is getAllCategories, page = %d and limit = %d", page, limit));
    }

    //http://localhost:8088/api/v1/categories
    @PostMapping("")
    public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO category,
                                                 BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok("This is insertCategory " + category.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //http://localhost:8088/api/v1/categories/7
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id) {
        return ResponseEntity.ok(String.format("updateCategory with id = %d", id));
    }

    //http://localhost:8088/api/v1/categories/7
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(String.format("deleteCategory with id = %d", id));
    }
}
