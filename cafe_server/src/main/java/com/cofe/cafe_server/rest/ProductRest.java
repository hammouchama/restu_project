package com.cofe.cafe_server.rest;

import com.cofe.cafe_server.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/product")
public interface ProductRest {


    @PostMapping("/add")
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap);
    @GetMapping("/get")
    public ResponseEntity<List<ProductWrapper>> getAllProduct();

    @PutMapping("/update")
    public ResponseEntity<String > updateProduct(@RequestBody Map<String, String> requestMap);

    @DeleteMapping("/delete/{id}")
     ResponseEntity<String> deleteProduct(@PathVariable long id);

    @PutMapping("/updateStatus")
     ResponseEntity<String> updateStatus(@RequestBody Map<String ,String> requistMap);

    @GetMapping("/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable long id);

    @GetMapping("/getById/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable long id);
}
