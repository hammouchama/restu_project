package com.cofe.cafe_server.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductWrapper {

    Long id;
    String name;
    String description;
    String status;
    float price;
    long categoryId;
    String categoryName;

    public ProductWrapper(Long id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
