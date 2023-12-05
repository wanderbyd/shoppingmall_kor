package com.shoppingmall.orders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingmall.cart.Cart;
import com.shoppingmall.cart.CartRepository;
import com.shoppingmall.cart.CartService;
import com.shoppingmall.item.Item;
import com.shoppingmall.item.ItemRepository;
import com.shoppingmall.item.ItemService;
import com.shoppingmall.users.UserRepository;
import com.shoppingmall.users.Users;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;

@Service
public class OrdersService {

	private static final String users = null;
	@Autowired
	private final OrdersRepository ordersRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;

	@Autowired
	public OrdersService(OrdersRepository ordersRepository, CartRepository cartRepository,
			UserRepository userRepository, ItemRepository itemRepository) {

		this.ordersRepository = ordersRepository;
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.itemRepository = itemRepository;

	}

	@Transactional 
	public List<Long> transferCartToOrder(Long cartid, Long usersid) {
		Optional<Cart> optionalCart = cartRepository.findById(cartid);
		Map<Item, Long> cartItems = cartService.getItemsFromCart(cartid);

		if (optionalCart.isPresent() && cartItems != null && !cartItems.isEmpty()) {
			Cart cart = optionalCart.get();
			List<Long> orderIds = new ArrayList<>();
			Users loggedInUser = userRepository.findByUsersid(usersid).orElse(null);

			for (Map.Entry<Item, Long> entry : cartItems.entrySet()) {
				Orders order = new Orders();

				order.setUsersid(cart.getUsersid());
				// order.setCartid(cart.getCartid());
				order.setUsers(loggedInUser); // Set the Users in Orders
				order.setCart(cart); // Set the Cart in Orders

				Item item = entry.getKey();
				Long quantity = entry.getValue();

				order.setItem(item);
				order.setCartitem(Collections.singletonMap(item, quantity));
				order.setQuantity(quantity.intValue());
				order.setOrderprice((double) (item.getPrice() * order.getQuantity()));

				System.out.println("DEBUG: Order created service - " + order);

				Orders savedOrder = ordersRepository.save(order);
				orderIds.add(savedOrder.getOrderid());
			}

			return orderIds;
		} else {
			System.out.println("DEBUG: CartItem not found or empty");
			return Collections.emptyList(); // or throw an exception, depending on your use case
		}
	}

	public List<Orders> getAllOrders() {
		System.out.println("Fetching all orders...");

		List<Orders> allOrders = ordersRepository.findAll();

		System.out.println("Fetched " + allOrders.size() + " orders.");

		return allOrders;
	}

	public List<List<Orders>> getAllOrdersSortedAndGrouped() {
		// 모든 주문을 주문자로 그룹화하고, 그 안에서 날짜별로 그룹화하고 각 그룹 내에서 주문 날짜를 기준으로 정렬
		// List<Orders> allOrders = ordersRepository.findAllByOrderByOrderdateAsc();
		List<Orders> allOrders = ordersRepository.findAll(Sort.by(Sort.Direction.ASC, "orderdate"));

		Map<String, List<Orders>> groupedOrders = allOrders.stream()
				.filter(order -> order.getUsers() != null && order.getUsers().getEmail() != null)
				.collect(Collectors.groupingBy(order -> order.getUsers().getEmail()));

		// 정렬된 결과를 반환
		return groupedOrders.values().stream().map(userOrders -> userOrders.stream()
				.collect(Collectors.groupingBy(order -> order.getOrderdate().toLocalDate(), Collectors.toList()))
				.values().stream()
				.flatMap(ordersList -> ordersList.stream().sorted(
						Comparator.comparing(Orders::getOrderdate, Comparator.nullsLast(Comparator.reverseOrder()))))
				.peek(order -> System.out.println("Sorted Order: " + order)).collect(Collectors.toList()))

				.collect(Collectors.toList());
	}

	public List<Orders> getAllOrdersSortedByDate() {
		// You can add more sophisticated sorting logic here
		return ordersRepository.findAllByOrderByOrderdateAsc();
	}

}
