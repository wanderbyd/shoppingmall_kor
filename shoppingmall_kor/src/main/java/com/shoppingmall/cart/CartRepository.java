package com.shoppingmall.cart;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

      List<Cart> findByUsersid(Long usersid);
      public Optional<Cart> findCartByUsersid(Long usersid);
	
 
}