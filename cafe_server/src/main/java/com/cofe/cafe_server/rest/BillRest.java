package com.cofe.cafe_server.rest;

import com.cofe.cafe_server.POJO.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/bill")
public interface BillRest {


    @PostMapping("/generateReport")
    ResponseEntity<String > generateReport(@RequestBody Map<String ,Object> requestMap);

    @GetMapping("/getBills")
    ResponseEntity<List<Bill>> getBill();
    @PostMapping("/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String,Object> requestMap);
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String > deleteBill(@PathVariable long id);

}
