package com.cofe.cafe_server.repository;

import com.cofe.cafe_server.POJO.Product;
import com.cofe.cafe_server.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<ProductWrapper> getAllProduct();
    List<ProductWrapper> getProductByCategory(long id);

    ProductWrapper getProductById(long id);
}
