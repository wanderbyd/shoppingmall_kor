package com.shoppingmall.cart;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.shoppingmall.users.Users;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;



@Entity
@Table(name = "Cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CART_SEQ")
	@SequenceGenerator(name = "CART_SEQ", sequenceName = "CART_SEQ", allocationSize = 1)
	@Column(name = "cartid")
	private Long cartid;

	@JoinColumn(name = "usersid")
	private Long usersid;
	
	@ElementCollection
	@CollectionTable(name = "CART_CARTITEM", joinColumns = @JoinColumn(name = "cart_cartid"))
	@MapKeyColumn(name = "cartitem_key")
	@Column(name = "cartitem")
	@Cascade(value = CascadeType.ALL)
	private Map<Long, Long> cartitem = new HashMap<>();
	

	@ManyToOne
	@JoinColumn(name = "usersid", referencedColumnName = "usersid", insertable = false, updatable = false)
	public Users users;

	public Cart() {
		super();
	}

	public Cart(Long usersid) {
		this.usersid = usersid;
		this.cartitem = new HashMap<>();
	}

	public Long getCartid() {
		return cartid;
	}

	public void setCartid(Long cartid) {
		this.cartid = cartid;
	}

	public Long getUsersid() {
		return usersid;
	}

	public void setUsersid(Long usersid) {
		this.usersid = usersid;
	}

	public Map<Long, Long> getCartitem() {
		return cartitem;
	}

	public void setCartitem(Map<Long, Long> cartitem) {
		this.cartitem = cartitem;
	}

	@Override
	public String toString() {
		return "Cart [cartid=" + cartid + ", usersid=" + usersid + ", cartitem=" + cartitem + "]";
	}

}
