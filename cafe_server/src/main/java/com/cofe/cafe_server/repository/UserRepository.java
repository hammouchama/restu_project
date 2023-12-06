package com.cofe.cafe_server.repository;

import com.cofe.cafe_server.POJO.User;
import com.cofe.cafe_server.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(@Param("email") String email);

    List<UserWrapper> getAllUsers();
    List<String> getAllAdmin();
    @Transactional
    @Modifying
    void updateStatus(@Param("status") String status,@Param("id") long id);

}
