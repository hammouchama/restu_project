package com.cofe.cafe_server.POJO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "User.getAllUsers",query = "select new com.cofe.cafe_server.wrapper.UserWrapper(u.id,u.name,u.email,u.address,u.status) from User u where u.role='user'")

@NamedQuery(name = "User.getAllAdmin",query = "select u.email from User u where u.role='admin'")

@NamedQuery(name = "User.updateStatus",query = "update User u set u.status=:status where  u.id=:id")

@Entity
@DynamicInsert
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")

public class User {
    private static final long serialVersionUID=1l;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String status;
    private String role;
}
