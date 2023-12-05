package com.shoppingmall.review;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shoppingmall.item.Item;
import com.shoppingmall.users.Users;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
   
    List<Review> findByItem(Optional<Item> item);
    List<Review> findByUsers(Users users);

    @Modifying
    @Query("UPDATE Review r SET r.displayNumber = :displayNumber WHERE r.rvid = :rvid")
    void updateDisplayNumber(@Param("rvid") Long rvid, @Param("displayNumber") Integer displayNumber);
	
  //화면에 순서대로 찍히도록!
    List<Review> findByItemOrderByRvidAsc(Optional<Item> item);

}
