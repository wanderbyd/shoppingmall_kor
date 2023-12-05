package com.shoppingmall.orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.Cart;
 
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUsers_Usersid(Long usersid);
    @Query("SELECT o FROM Orders o")
    List<Orders> findAll();
    List<Orders> findAllByOrderByOrderdateAsc();

}