package com.shoppingmall.qna;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
	List<Qna> findByUsersid(Long usersid);
	List<Qna> findByItemid(Long itemid);
}
