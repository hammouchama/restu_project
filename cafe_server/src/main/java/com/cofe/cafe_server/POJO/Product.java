package com.cofe.cafe_server.POJO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.DataTruncation;

@NamedQuery(name = "Product.getAllProduct",query = "select new com.cofe.cafe_server.wrapper.ProductWrapper(p.id,p.name,p.description,p.status,p.price,p.category.id,p.category.name) from Product p")

@NamedQuery(name = "Product.getProductByCategory",query = "select new com.cofe.cafe_server.wrapper.ProductWrapper(p.id,p.name,p.description,p.status,p.price,p.category.id,p.category.name) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Product.getProductById",query = "select new com.cofe.cafe_server.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk",nullable = false)
    private Category category;

    private String description;

    private float price;

    private String status;

}
