package com.cvvalidator.cvvalidator.controller;

import com.cvvalidator.cvvalidator.model.Category;
import com.cvvalidator.cvvalidator.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController implements IRestController<Category> {

    //region Init
    @Autowired
    private CategoryRepository categoryRepository;
    //endregion

    //region Category

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<Iterable<Category>> get() {
        try {
            Iterable<Category> categories = categoryRepository.findAll();
            return ResponseEntity.ok().body(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> getById(@PathVariable long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                return ResponseEntity.ok().body(category.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> create(@RequestBody Category newCategory) {
        try {
            newCategory = categoryRepository.save(newCategory);
            return ResponseEntity.created(new URI("")).body(newCategory);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity delete(@PathVariable long id) {
        try
        {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                categoryRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> update(long id,Category newCategory) {
        try{
            Optional<Category> optional = categoryRepository.findById(id);
            if(optional.isPresent())
            {
                Category category = optional.get();
                category.setName(newCategory.getName());
                category.setDescription(newCategory.getDescription());
                categoryRepository.save(newCategory);
                return ResponseEntity.noContent().build();
            }
            else
            {
                Category category = categoryRepository.save(newCategory);
                return ResponseEntity.created(new URI("")).body(category);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    //endregion
}
