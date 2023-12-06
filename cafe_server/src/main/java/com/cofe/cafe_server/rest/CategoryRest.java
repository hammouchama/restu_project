package com.cofe.cafe_server.rest;

import com.cofe.cafe_server.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/category")
public interface CategoryRest {

    @PostMapping("/add")
    public ResponseEntity<String > addNewCategory(@RequestBody Map<String ,String > requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PutMapping("/update")
    public ResponseEntity<String > updateCategory(@RequestBody Map<String ,String > requesMap);
}
