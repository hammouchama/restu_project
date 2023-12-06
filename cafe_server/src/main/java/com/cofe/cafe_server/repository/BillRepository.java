package com.cofe.cafe_server.repository;

import com.cofe.cafe_server.POJO.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {

    List<Bill> findByEmail(String email);
}
