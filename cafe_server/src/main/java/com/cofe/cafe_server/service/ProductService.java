package com.cofe.cafe_server.service;

import com.cofe.cafe_server.JWT.JwtFilter;
import com.cofe.cafe_server.POJO.Category;
import com.cofe.cafe_server.POJO.Product;
import com.cofe.cafe_server.constents.CafeConstants;
import com.cofe.cafe_server.repository.ProductRepository;
import com.cofe.cafe_server.utils.CafeUtils;
import com.cofe.cafe_server.wrapper.ProductWrapper;
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
public class ProductService {
    ProductRepository productRepository;
    JwtFilter jwtFilter;
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {

        try {
            if (jwtFilter.isAdmin()){
              if (valideteProductMap(requestMap,false)){
                productRepository.save(getProductFromMap(requestMap,false));
                return CafeUtils.getResponseEntity("Product Added Successfully",HttpStatus.OK);
              }else {
                  return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
              }
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED8ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }




    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
     try {
         return new ResponseEntity<>(productRepository.getAllProduct(),HttpStatus.OK );

     }catch (Exception ex){
         ex.printStackTrace();
     }

     return new ResponseEntity<>(new ArrayList<>() ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
      try {
          if(jwtFilter.isAdmin()){
              if (valideteProductMap(requestMap,true)){
                  Optional<Product> optionalProduct= productRepository.findById(Long.parseLong(requestMap.get("id")));
                  if(!optionalProduct.isEmpty()){
                       Product product=getProductFromMap(requestMap,true);
                       product.setStatus(optionalProduct.get().getStatus());
                       productRepository.save(product);
                       return CafeUtils.getResponseEntity("Product Updated Successfully!",HttpStatus.OK);
                  }else {
                      return CafeUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
                  }
              }else {
                  return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
              }
          }else {
              return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED8ACCESS,HttpStatus.UNAUTHORIZED);
          }
      }catch (Exception ex){
          ex.printStackTrace();
      }
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
     }

    private boolean valideteProductMap(Map<String, String> requestMap, boolean b) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && b){
                return true;
            }else if (!b){
                return true;
            }
        }
        return false;

    }
    private Product getProductFromMap(Map<String, String> requestMap, boolean b) {
        Category category=new Category();
        category.setId(Long.parseLong(requestMap.get("categoryId")));
        Product product=new Product();
        if (b){
            product.setId(Long.parseLong(requestMap.get("id")));
        }else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Float.parseFloat(requestMap.get("price")));

        return product;
    }

    public ResponseEntity<String> deleteProduct(long id) {
        try {
           if (jwtFilter.isAdmin()){
                Optional<Product> pp=productRepository.findById(id);
                if (!pp.isEmpty()){
                    productRepository.deleteById(id);
                    return CafeUtils.getResponseEntity("Product Deleted Successfully!",HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity("Product id does not exist ",HttpStatus.OK);
                }
           }else {
               return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED8ACCESS,HttpStatus.UNAUTHORIZED);
           }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> updateSatatus(Map<String, String> requistMap) {
        try {
            if (jwtFilter.isAdmin()){
                Optional<Product> p=productRepository.findById(Long.parseLong(requistMap.get("id")));
                if(!p.isEmpty()){
                     p.get().setStatus(requistMap.get("status"));
                     productRepository.save(p.get());
                    return CafeUtils.getResponseEntity("Product Status Updated Successfully",HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
                }
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED8ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<ProductWrapper>> getByCategory(long id) {
        try{
            return new ResponseEntity<>(productRepository.getProductByCategory(id),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>() ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ProductWrapper> getById(long id) {
        try {
            return new ResponseEntity<>(productRepository.getProductById(id),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
