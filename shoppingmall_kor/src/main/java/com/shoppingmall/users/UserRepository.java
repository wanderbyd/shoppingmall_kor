package com.shoppingmall.users;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.Cart;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	@Override
	public List<Users> findAll();

	public Optional<Users> findFirstByEmail(String email);

	public Optional<Users> findByUsersid(Long usersid);

	//public Optional<Cart> findByUsersid(Users loggedInUser2);

}