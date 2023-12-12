package com.cofe.cafe_server.wrapper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class UserWrapper {

    private Long id;

    private String name;

    private String email;

    private String address;

    private  String  status;

    public UserWrapper(Long id, String name, String email, String address, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.status = status;
    }
}
