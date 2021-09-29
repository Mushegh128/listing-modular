package com.modular.rest.endpoints;

import com.listing.common.model.Category;
import com.listing.common.servicies.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CategoryEndpoint {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity getCategories() {
        List<Category> allCategories = categoryService.findAll();
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") int id) {
        Optional<Category> byId = categoryService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(byId.get());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.save(category));
    }

    @PutMapping("/categories")
    public ResponseEntity<Category> changeName(@RequestBody Category category) {
        Optional<Category> byId = categoryService.findById(category.getId());
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category categoryFromBD = byId.get();
        categoryFromBD.setName(category.getName());
        return ResponseEntity.ok(categoryService.save(categoryFromBD));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity deleteCategory(@PathVariable("id") int id) {
        if (categoryService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
