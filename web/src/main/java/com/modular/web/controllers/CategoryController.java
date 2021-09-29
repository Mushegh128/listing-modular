package com.modular.web.controllers;

import com.listing.common.model.Category;
import com.listing.common.servicies.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String getCategories(ModelMap modelMap) {
        List<Category> allCategories = categoryService.findAll();
        modelMap.addAttribute("allCategories", allCategories);
        return "categories";
    }

    @GetMapping("/categories/{id}")
    public String getCategory(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Category> byId = categoryService.findById(id);
        modelMap.addAttribute("category", byId.get());
        return "singleCategory";
    }

    @PostMapping("/categories")
    public String addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return "redirect:/categories";
    }

    @PutMapping("/categories")
    public String changeName(@RequestBody Category category) {
        Optional<Category> byId = categoryService.findById(category.getId());
        if (byId.isEmpty()) {
            return "redirect:/categories";
        }
        Category categoryFromBD = byId.get();
        categoryFromBD.setName(category.getName());
        return "redirect:/categories";
    }

    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        return "redirect:/categories";
    }

}
