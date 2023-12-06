package com.cofe.cafe_server.service;

import com.cofe.cafe_server.JWT.JwtFilter;
import com.cofe.cafe_server.POJO.Category;
import com.cofe.cafe_server.constents.CafeConstants;
import com.cofe.cafe_server.repository.CategoryRepository;
import com.cofe.cafe_server.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    CategoryRepository categoryRepository;

    JwtFilter jwtFilter;
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
       try{
          if(jwtFilter.isAdmin()){
              if(validateCategoryMap(requestMap,false)){
                categoryRepository.save(getCategoryFromMap(requestMap,false));

                return CafeUtils.getResponseEntity("Category Added Successfully a",HttpStatus.OK);
              }
          }else {
              return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED8ACCESS,HttpStatus.UNAUTHORIZED);
          }
       }catch (Exception e){
           e.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
     if (requestMap.containsKey("name")){
         if (requestMap.containsKey("id") && validateId)
             return true;
         else if (!validateId)
             return true;

     }
        return false;
    }
    private Category getCategoryFromMap(Map<String,String> map,Boolean isAdd){
        Category category=new Category();
        if (isAdd){
            category.setId(Long.parseLong(map.get("id")));
        }
        category.setName(map.get("name"));

        return category;
    }

    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                 return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> updateCategory(Map<String, String> requesMap) {
        try {
             if (jwtFilter.isAdmin()){
                 if(validateCategoryMap(requesMap,true)){
                   Optional op= categoryRepository.findById(Long.parseLong(requesMap.get("id")));
                   if (!op.isEmpty()){
                        categoryRepository.save(getCategoryFromMap(requesMap,true));

                        return CafeUtils.getResponseEntity("Category Updatet Successfully ",HttpStatus.OK);
                   }else {
                       return CafeUtils.getResponseEntity("Category if does not exist ",HttpStatus.OK);
                   }
                 }else {
                     return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
                 }
             }else {
                 return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED8ACCESS,HttpStatus.UNAUTHORIZED);
             }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
