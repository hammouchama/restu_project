package com.cofe.cafe_server.rest;

import com.cofe.cafe_server.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserRest {
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String ,String >requestMap);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String ,String> requesMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody Map<String , String> requestMap);


    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken();

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String ,String > requestMap);

    @PostMapping("/forgotPassword")
    public ResponseEntity<String > forgotPassword(@RequestBody Map<String ,String> requestMap);

  }
