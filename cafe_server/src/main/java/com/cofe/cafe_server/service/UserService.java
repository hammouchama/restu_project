package com.cofe.cafe_server.service;

import com.cofe.cafe_server.JWT.CustomerUserDetailsService;
import com.cofe.cafe_server.JWT.JwtFilter;
import com.cofe.cafe_server.JWT.JwtUtil;
import com.cofe.cafe_server.POJO.User;
import com.cofe.cafe_server.constents.CafeConstants;
import com.cofe.cafe_server.repository.UserRepository;
import com.cofe.cafe_server.utils.CafeUtils;
import com.cofe.cafe_server.utils.EmailUtils;
import com.cofe.cafe_server.wrapper.UserWrapper;
import lombok.AllArgsConstructor;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    AuthenticationManager authenticationManager;

    CustomerUserDetailsService customerUserDetailsService;

    JwtUtil jwtUtil;

    JwtFilter jwtFilter;

    BCryptPasswordEncoder passwordEncoder;

    EmailUtils emailUtils;

    public ResponseEntity<String> signUp(Map<String ,String> requestMap){
         try {
             if (validateSignUpMap(requestMap)) {
                 User user = userRepository.findByEmail(requestMap.get("email"));
                 if (Objects.isNull(user)) {
                     userRepository.save(getUserFromMap(requestMap));
                     return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                 } else {
                     return CafeUtils.getResponseEntity("Email already exist ", HttpStatus.BAD_REQUEST);
                 }
             } else {
                 return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
             }
         }catch (Exception e){
             e.printStackTrace();
         }
         return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateSignUpMap(Map<String,String> reqMap){
        return  reqMap.containsKey("name") && reqMap.containsKey("contactNumber")
                && reqMap.containsKey("email") &&reqMap.containsKey("password");
    }
    private User getUserFromMap(Map<String,String> requestMap){
        User user=new User();
        user.setName(requestMap.get("name"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    public ResponseEntity<String> logIn(Map<String ,String> requestMap){
         try {
             Authentication auth=authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
             );
             if(auth.isAuthenticated()){
                 if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                     return new ResponseEntity<>("{\"token\":\""+
                             jwtUtil.genreateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                     customerUserDetailsService.getUserDetail().getRole())+"\",\"role\":\""+customerUserDetailsService.getUserDetail().getRole()+"\"}",HttpStatus.OK);
                 }else {
                     return new ResponseEntity<>("\"message\":\""+"Wait for admin approval. "+"\"}",HttpStatus.BAD_REQUEST);
                 }
             }
         }catch (Exception ex){
             ex.printStackTrace();
         }
        return new ResponseEntity<>("{\"message\":\""+"Bad Credentials. "+"\"}",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
         if (jwtFilter.isAdmin()){
            return  new ResponseEntity<>(userRepository.getAllUsers(),HttpStatus.OK);
         }else {
             return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
         }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
              Optional<User> optionalUser=userRepository.findById(Long.parseLong(requestMap.get("id")));
              if (!optionalUser.isEmpty()){
                 userRepository.updateStatus(requestMap.get("status"),Long.parseLong(requestMap.get("id")));
                 sendMailToAllAdmin(requestMap.get("status"),optionalUser.get().getEmail(),userRepository.getAllAdmin());
                 return CafeUtils.getResponseEntity("User Status Update Successfully",HttpStatus.OK);
              }else {
                  return CafeUtils.getResponseEntity("User id doesn't exist ",HttpStatus.OK);
              }
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED8ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(
                CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status!=null && status.equalsIgnoreCase("true")){
           emailUtils.sendSimplMessage(jwtFilter.getCurrentUser(), "Account Approved ","USER:- "+user+" \n is approved by \nADMIN:- "+jwtFilter.getCurrentUser(),allAdmin);
        }else {
            emailUtils.sendSimplMessage(jwtFilter.getCurrentUser(), "Account Disabled ","USER:- "+user+" \n is disabled by \nADMIN:- "+jwtFilter.getCurrentUser(),allAdmin);
        }
    }

    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("True ",HttpStatus.OK);
    }

    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
             User user=userRepository.findByEmail(jwtFilter.getCurrentUser());
             if (!user.equals(null)){
                 if(passwordEncoder.matches(requestMap.get("oldPassword"),user.getPassword())){

                    user.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                    userRepository.save(user);
                    return CafeUtils.getResponseEntity("Password Updated Successfully",HttpStatus.OK);
                 }
                 return CafeUtils.getResponseEntity("Incorrect Old Password",HttpStatus.BAD_REQUEST);
             }
             return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> fogotPassword(Map<String, String> requestMap) {
        try {
            User user=userRepository.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isEmpty(user.getEmail())){
               emailUtils.forgotMail(user.getEmail(),"Credentials By Oussama Hammouchama " , user.getPassword());
            }
            return CafeUtils.getResponseEntity("Check your mail for Credentials. ",HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
