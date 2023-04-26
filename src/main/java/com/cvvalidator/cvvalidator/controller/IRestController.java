package com.cvvalidator.cvvalidator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IRestController<T> {
    @GetMapping("")
    public @ResponseBody ResponseEntity<Iterable<T>> get();
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<T> getById(@PathVariable long id);
    @PostMapping("")
    public @ResponseBody
    ResponseEntity<?> create(@RequestBody T newEntity);
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id);
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable long id,@RequestBody T newEntity);
}
