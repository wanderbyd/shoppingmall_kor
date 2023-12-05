package com.shoppingmall.general;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.shoppingmall.item.Item;
import com.shoppingmall.item.ItemService;
import com.shoppingmall.users.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	private ItemService itemService;

	@GetMapping("/") // 메인페이지
	public String mainPage(Model model, Users users, HttpSession session) {
		List<Item> items = new ArrayList<>();
		for (Item i : itemService.findAllItems()) {
			if (items.size() < 6) {
				items.add(i);
			}
		}
		  String loggedInUserId = (String) session.getAttribute("loggedInUserId");

	        model.addAttribute("loggedInUserId", loggedInUserId);
		model.addAttribute("users", users);
		model.addAttribute("items", items);
		return "main";
	}

	@GetMapping("/contact")
	public String contactPage(Model model, Users users,  HttpSession session) {
		model.addAttribute("users", users);
		
		  String loggedInUserId = (String) session.getAttribute("loggedInUserId");

	        model.addAttribute("loggedInUserId", loggedInUserId);
		return "contact";
	}
}
