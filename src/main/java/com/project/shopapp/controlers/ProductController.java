package com.project.shopapp.controlers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertProduct(@Valid @ModelAttribute ProductDTO product,
//                                            @RequestPart("file") MultipartFile file,
                                            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            //Check file size and format
            MultipartFile file = product.getFile();

            if(file != null) {
                if(file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB.");
                }

                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image!");
                }

                //Save file and update thumbnail in DTO
                String fileName = storeFile(file);

                //Save to product object in database -> do it later

            }

            return ResponseEntity.ok("This is insertProduct " + product.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //Add UUID before file name to ensure file name is unique.
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        //Path to the folder where you want to save the file
        //import java.nio.file.*
        Path uploadDir = Paths.get("uploads");

        //Check and create the directory if it doesn't exist
        //import java.nio.file.*
        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        //Full path to file
        //destination: đích đến, đích
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);

        //Copy file to destination folder
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

//    {
//        "name": "Remote TV",
//        "price": 1.6,
//        "thumbnail": "ccccc/",
//        "description": "Product to control TV",
//        "category_id": 1
//    }

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
