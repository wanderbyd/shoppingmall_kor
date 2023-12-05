package com.shoppingmall.users;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingmall.cart.CartService;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private CartService cartService;

	// 
//	public Users createUser(Users users) {
//		
//		return userRepository.save(users);
//	}

	//사용자 가입시 카트 자동생성으로 수정//이 부분 수정했음//오더 때문에
	public Users createUser2(Users users) {
	    Users savedUser = userRepository.save(users);
	    
	    // 사용자 등록 후 자동으로 카트 생성
	    cartService.createCart(savedUser.getUsersid());

	    return savedUser;
	}

	//사용자생성 원본//오더때문에 닫음
//	public void createUser2(Users users) {
//		
//		
//		userRepository.save(users);
//	}

	// 사용자찾기
	public Optional<Users> findUserById(Long usersid) {
		return userRepository.findByUsersid(usersid);
	}

	// 모든사용자 찾기
	public List<Users> findAllUsers() {
		return userRepository.findAll();
	}

	// 사용자업데이트
	public Users updateUser(Users users) {
		return userRepository.save(users);
	}

	// 사용자삭제
	public void deleteUser(Long usersid) {
		userRepository.deleteById(usersid);
	}

	// 이메일
	public Optional<Users> findUserByEmail(String email) {
		if (email == null)
			throw new NullPointerException("EMail must not be null.");
		if (email.isEmpty())
			throw new NullPointerException("EMail must not be empty.");

		return userRepository.findFirstByEmail(email.toLowerCase().trim());
	}

	// 이메일중복방지//이건됨
	public boolean doesEmailAlreadyExists(final String email) {
		if (email == null)
			throw new NullPointerException("Email must not be null.");
		if (email.isEmpty())
			throw new NullPointerException("Email must not be empty.");

		return findUserByEmail(email).isPresent();
	}

	// 추가

	public static String hashPassword(String password) {

		if (password == null) {

			throw new IllegalArgumentException("Password cannot be null");

		}

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// Hash the password

			byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

			// Convert the byte array to a hexadecimal representation

			StringBuilder hexString = new StringBuilder(2 * hashedBytes.length);

			for (byte b : hashedBytes) {

				String hex = Integer.toHexString(0xff & b);

				if (hex.length() == 1) {

					hexString.append('0');

				}

				hexString.append(hex);

			}

			System.out.println(hexString);

			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

			return null;

		}

	}

	// 패스워드 업데이트

	public void updatePassword(final String newPassword, final Users users) {

		if (newPassword == null)

			throw new NullPointerException("NewPassword must not be null.");

		if (newPassword.isEmpty())

			throw new IllegalArgumentException("NewPassword must not be empty.");

		if (users == null)

			throw new NullPointerException("User must not be null.");

		String hashedPassword = hashPassword(newPassword);

		if (hashedPassword != null) {

			users.setHashedpassword(hashedPassword);

			saveUser(users);

		} else {

			// 처리할 예외 상황에 대한 로직 추가

		}

	}

	// 패스워드 재해싱

	public void rehashPassword(final String password, final Users users) {

		this.updatePassword(password, users);

	}

	public void saveUser(final Users users) {

		if (users == null)

			throw new NullPointerException("User must not be null");

		userRepository.save(users);

	}

}