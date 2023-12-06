package com.cofe.cafe_server.service;

import com.cofe.cafe_server.repository.BillRepository;
import com.cofe.cafe_server.repository.CategoryRepository;
import com.cofe.cafe_server.repository.ProductRepository;
import com.cofe.cafe_server.utils.CafeUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DashboardService {

    ProductRepository productRepository;

    CategoryRepository categoryRepository;

    BillRepository billRepository;
    public ResponseEntity<Map<String, Object>> getCount() {
        try {
            Map<String,Object> map=new HashMap<>();
            map.put("category",categoryRepository.count());
            map.put("product",productRepository.count());
            map.put("bill",billRepository.count());

            return new ResponseEntity<>(map, HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
