package com.shoppingmall.cart;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingmall.item.Item;
import com.shoppingmall.users.Users;


import com.shoppingmall.item.ItemService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private CartService cartService;
	@Autowired
	private ItemService itemService;
	

	@GetMapping("/cart")//오더 때문에 약간의 수정을 했음 데이터베이스에 겹치지 않도록
	public String showCart(Model model, HttpSession session, @RequestParam("itemid") Optional<Long> itemid,
	        HttpServletRequest request, HttpServletResponse response) {
	    System.out.println("start");
	    String contextPath = request.getContextPath();
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        return "redirect:/login";
	    }

	    Cart cart = null;

	    if (itemid.isEmpty()) {
    	
	    	   Optional<Cart> optionalCart = cartService.getUserCart(loggedInUser.getUsersid());
	    	    if (optionalCart.isPresent()) {
	    	        cart = optionalCart.get();
	    	    } else {
	    	        cartService.createCart(loggedInUser.getUsersid());
	    	        cart = cartService.getUserCart(loggedInUser.getUsersid()).orElseThrow(() -> new RuntimeException("Failed to create or retrieve the user's cart"));
	    	    }
	    } else {
	        if (cartService.hasUserCart(loggedInUser.getUsersid())) {
	            cartService.addItemToCart(loggedInUser.getUsersid(), itemid.get(), 1L, request, response);
	            cart = cartService.getUserCart(loggedInUser.getUsersid()).orElse(null);
	        } else {
	            cartService.createCart(loggedInUser.getUsersid());
	            cartService.addItemToCart(loggedInUser.getUsersid(), itemid.get(), 1L, request, response);
	            cart = cartService.getUserCart(loggedInUser.getUsersid()).orElse(null);
	        }
	    }

	    Map<Item, Long> cartitem = cartService.getItemsFromCart(cart.getCartid());
	    model.addAttribute("cartitem", cartitem);
	    model.addAttribute("total", cartService.calculateTotalPrice(cartitem));
	    model.addAttribute("cartid", cart.getCartid());

	    return "cart/cartitem";
	}



//	@GetMapping("/cart") // 카트 아이디를 가져와 보도록 할것 // 이 코드가 최근//원본
//	public String showCart(Model model, HttpSession session, @RequestParam("itemid") Optional<Long> itemid,
//			HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("start");
//		String contextPath = request.getContextPath();
//		Users loggedInUser = (Users) session.getAttribute("loggedInUser");
//
//		if (loggedInUser == null) {
//			return "redirect:/login";
//		}
//
//		Cart cart = null;
//		if (itemid.isEmpty()) {
//			if (cartService.hasUserCart(loggedInUser.getUsersid())) {
//				cart = cartService.getUserCart(loggedInUser.getUsersid()).get();
//				Map<Item, Long> cartitem = cartService.getItemsFromCart(cart.getCartid());
//				model.addAttribute("cartitem", cartitem);
//				model.addAttribute("total", cartService.calculateTotalPrice(cartitem));
//				model.addAttribute("cartid", cart.getCartid());
//			} else {
//				cartService.createCart(loggedInUser.getUsersid());
//				cart = cartService.getUserCart(loggedInUser.getUsersid()).get();
//				Map<Item, Long> cartitem = cartService.getItemsFromCart(cart.getCartid());
//				model.addAttribute("cartitem", cartitem);
//				model.addAttribute("total", cartService.calculateTotalPrice(cartitem));
//				model.addAttribute("cartid", cart.getCartid());
//			}
//		} else {
//			if (cartService.hasUserCart(loggedInUser.getUsersid())) {
//				cartService.addItemToCart(loggedInUser.getUsersid(), itemid.get(), 1L, request, response);
//				cart = cartService.getUserCart(loggedInUser.getUsersid()).get();
//				Map<Item, Long> cartitem = cartService.getItemsFromCart(cart.getCartid());
//				model.addAttribute("cartitem", cartitem);
//				model.addAttribute("total", cartService.calculateTotalPrice(cartitem));
//				model.addAttribute("cartid", cart.getCartid());
//			} else {
//				cartService.createCart(loggedInUser.getUsersid());
//				cartService.addItemToCart(loggedInUser.getUsersid(), itemid.get(), 1L, request, response);
//				cart = cartService.getUserCart(loggedInUser.getUsersid()).get();
//				Map<Item, Long> cartitem = cartService.getItemsFromCart(cart.getCartid());
//				model.addAttribute("cartitem", cartitem);
//				model.addAttribute("total", cartService.calculateTotalPrice(cartitem));
//				model.addAttribute("cartid", cart.getCartid());
//			}
//		}
////model.addAttribute("users", users);
//		return "cart/cartitem";
//
//	}

	@GetMapping("/payment")
	public String showPayment(Model model, Users users, @RequestParam("cartid") Optional<Long> cartid) {
		model.addAttribute("users", users);
		cartid.ifPresent(aLong -> cartService.removeItemFromStock(aLong));

		return "order/payment";
	}
	

	@GetMapping("/removeFromCart")
	public String removeFromCart(Model model, HttpSession session, @RequestParam("itemid") Optional<Long> itemid,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("delete");
		String contextPath = request.getContextPath();
		Users loggedInUser = (Users) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return "redirect:/login";
		}

		if (itemid.isEmpty())
			return "redirect:/cart";

		cartService.removeItemFromCart(loggedInUser.getUsersid(), itemid.get(), request, response); // 'removeItemToCart'
																									// ->
																									// 'removeItemFromCart'
		return "redirect:/cart";
	}

	@PostMapping("/updateQuantity")
	public String updateCartItemQuantity(@RequestParam String cartid, @RequestParam String itemid,
			@RequestParam String newQuantity, RedirectAttributes attributes) {
		try {
			cartService.updateCartItemQuantity(Long.parseLong(cartid), Long.parseLong(itemid),
					Long.parseLong(newQuantity));
			attributes.addFlashAttribute("message", "Cart item quantity updated successfully");
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Error updating cart item quantity");
		}

		// 다시 원래 페이지로 리디렉션
		return "redirect:/cart"; // 여기서 "yourPreviousPageURL"은 실제 이전 페이지의 URL로 대체되어야 합니다.
	}

}