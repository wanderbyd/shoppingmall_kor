package com.shoppingmall.users;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
//import javax.persistence.*;

@Entity
@Table(name = "Users")
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
	@SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
	@Column(name = "usersid")
	private Long usersid;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "email")
	private String email;

	@Column(name = "hashedpassword")
	private String hashedpassword;



	@Column(name = "address", nullable = false)
	private String address;
	

	@SuppressWarnings("unused")
	public Users() {
	};

	public Users(String firstname, String lastname, String email, String hashedpassword 
	
			, String address
			) {

		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.hashedpassword = hashedpassword;
		this.address = address;
	
	}

	public Long getUsersid() {
		return usersid;
	}

	public void setUsersid(Long usersid) {
		this.usersid = usersid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHashedpassword() {
		return hashedpassword;
	}

	public void setHashedpassword(String hashedpassword) {
		this.hashedpassword = hashedpassword;
	}




	@Override
	public String toString() {
		return "Users [usersid=" + usersid + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", hashedpassword=" + hashedpassword +
				"address" +address
			 
				+ "]";
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public Object getCartid() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}








