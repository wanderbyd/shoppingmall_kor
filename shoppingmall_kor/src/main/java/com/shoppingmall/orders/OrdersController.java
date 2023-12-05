package com.shoppingmall.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.cart.Cart;
import com.shoppingmall.cart.CartService;
import com.shoppingmall.item.Item;
import com.shoppingmall.item.ItemService;
import com.shoppingmall.users.Users;

import jakarta.servlet.http.HttpSession;

@Controller
//@RequestMapping("/orders")
public class OrdersController {
	

	@Autowired
	private CartService cartService;
	@Autowired
	private ItemService itemService;
	@Autowired
	  private final OrdersService ordersService;

	    @Autowired
	    public OrdersController(OrdersService ordersService) {
	        this.ordersService= ordersService;
	    }
	    
	    
	    @PostMapping("/orders/place-order")//카트 아이디도 들어옴//유저 아이디 넣어짐
	    public ResponseEntity<String> placeOrder(@RequestParam Long cartid, HttpSession session) {
	        // Check if the user is logged in
	        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	        System.out.println("DEBUG: loggedInUser - " + loggedInUser);
	        if (loggedInUser != null && loggedInUser.getUsersid() != null) {
	            // Fetch the cart
	            Optional<Cart> optionalCart = cartService.getUserCart(loggedInUser.getUsersid());

	            if (optionalCart.isPresent()) {
	                System.out.println("DEBUG: Cart found - " + optionalCart.get());
	                List<Long> orderid = ordersService.transferCartToOrder(cartid, loggedInUser.getUsersid());
	               // Long orderid = ordersService.transferCartToOrder(cartid);
	                System.out.println("DEBUG: Orderid - " + orderid);

	                // Clear the cart
	                cartService.removeItemFromStock(cartid);
	                System.out.println("DEBUG: Cart cleared");
	            } else {
	                // Handle the case when the cart is not found
	                // You can throw an exception, log a message, or handle it based on your use case
	                System.out.println("DEBUG: Cart not found");
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
	            }
	        } else {
	            // Handle the case when the user is not logged in or usersid is null
	            // You can throw an exception, log a message, or handle it based on your use case
	            System.out.println("DEBUG: User not logged in or usersid is null");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in or usersid is null");
	        }

	        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/payment").build();
	    }
	    

	    
	    @GetMapping("/allOrders")//최종1
	    public String getAllOrders(Model model, HttpSession session) {
	        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	        System.out.println("DEBUG: loggedInUser - " + loggedInUser);

	        if (loggedInUser != null) {
	            // Retrieve sorted orders directly from the service
	          

	            // Grouping logic remains the same as before
	            Map<String, Map<String, List<Orders>>> groupedByUserAndDateTime = new HashMap<>();
	            List<Orders> sortedOrders = ordersService.getAllOrdersSortedByDate();
	            for (Orders order : sortedOrders) {
	              //  double totalOrderPrice = 0.0;
	                String userEmail = order.getUsers().getEmail();
	                String dateTimeKey = order.getOrderdate().toString();

	                groupedByUserAndDateTime
	                        .computeIfAbsent(userEmail, k -> new HashMap<>())
	                        .computeIfAbsent(dateTimeKey, k -> new ArrayList<>())
	                        .add(order);

	               // totalOrderPrice += order.getOrderprice();
	            }

	            // Sorting orders within each user and date-time group
	            groupedByUserAndDateTime.forEach((userEmail, dateTimeOrdersMap) -> {
	                dateTimeOrdersMap.forEach((dateTimeKey, ordersList) -> {
	                    List<Orders> sortedOrdersWithinGroup = ordersList.stream()
	                            .sorted(Comparator.comparing(Orders::getOrderdate))
	                            .collect(Collectors.toList());
	                    dateTimeOrdersMap.put(dateTimeKey, sortedOrdersWithinGroup);
	                });
	            });

	            model.addAttribute("groupedByUserAndDateTime", groupedByUserAndDateTime);
	            model.addAttribute("loggedInUser", loggedInUser);

	            if ("admin@admin.com".equals(loggedInUser.getEmail())) {
	                return "order/allOrders";
	            } else {
	                return "order/orderdetail";
	            }
	        } else {
	            return "redirect:/login";
	        }
	    }
	    
	    @GetMapping("/sorted")
	    public ResponseEntity<List<Orders>> getAllOrdersSortedByDate() {
	        List<Orders> sortedOrders = ordersService.getAllOrdersSortedByDate();
	        return new ResponseEntity<>(sortedOrders, HttpStatus.OK);
	    }

}





	    
