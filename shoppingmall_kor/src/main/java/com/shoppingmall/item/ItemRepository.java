package com.shoppingmall.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	List<Item> findAll();

	List<Item> findByPriceBetween(Long min, Long max);

	Optional<Item> findByItemid(Long itemid);

	List<Item> findByCategory(Category category);

	@Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ESCAPE '!' OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')) ESCAPE '!'")
	List<Item> findByKeywordLike(@Param("keyword") String keyword);
}