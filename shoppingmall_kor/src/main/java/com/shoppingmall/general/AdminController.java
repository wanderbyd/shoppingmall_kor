package com.shoppingmall.general;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shoppingmall.item.Category;
import com.shoppingmall.item.Item;
import com.shoppingmall.item.ItemService;
import com.shoppingmall.users.Users;

@Controller
public class AdminController {
	@Autowired
	private ItemService itemService;

	@GetMapping("/addItem") // 상품추가 폼
	public String addItemForm(Model model, Users users) {
		// Item item = new Item();
		Category[] category = Category.values(); // 모든 카테고리 값들을 배열로 가져옴
		model.addAttribute("category", category); // 카테고리 값을 뷰로 전달
		model.addAttribute("users", users);
		model.addAttribute("item", new Item());
		return "item/itemcreate";
	}

	@GetMapping("/deleteItem") // 상품 삭제
	public String deleteItem(@RequestParam("itemid") Optional<Long> itemid, Users users) {
		if (itemid.isEmpty())
			return "redirect:/shop";

		Optional<Item> item = itemService.findItemById(itemid.get());

		if (item.isEmpty())
			return "redirect:/shop";
		itemService.deleteItem(itemid.get());
		return "redirect:/shop";

	}

	@PostMapping("/addItem") // 상품추가
	public String addItem(Model model, @ModelAttribute("item") Item formItem,
			@RequestParam("imageFile") MultipartFile imageFile, @RequestParam("imageFile2") MultipartFile imageFile2) {
		try {
			if (imageFile.isEmpty() || imageFile2.isEmpty()) {
				throw new IllegalArgumentException("파일을 선택해주세요.");
			}

			String uploadDir = "src/main/resources/static/images/imgfile/";

			String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
			String imagePath = uploadDir + imageName;
			Files.copy(imageFile.getInputStream(), Paths.get(imagePath));

			String imageName2 = UUID.randomUUID().toString() + "_" + imageFile2.getOriginalFilename();
			String imagePath2 = uploadDir + imageName2;
			Files.copy(imageFile2.getInputStream(), Paths.get(imagePath2));

			formItem.setImagePath("/images/imgfile/" + imageName);
			formItem.setImagePath2("/images/imgfile/" + imageName2);

			itemService.saveItem(formItem); // 아이템 저장

			return "redirect:/shop";
		} catch (IOException e) {
			e.printStackTrace();
			return "errorPage";
		}
	}

//	@GetMapping("/editItem") // 상품 수정 폼
//	public String editItemForm(@RequestParam("itemid") Long itemid, Model model, Users users) {
//		Optional<Item> item = itemService.findItemById(itemid);
//
//		if (item.isEmpty()) {
//			return "redirect:/shop";
//		}
//
//		model.addAttribute("users", users);
//		model.addAttribute("item", item.get());
//
//		return "item/itemedit";
//	}

//	@PostMapping("/editItem") // 상품 수정
//	public String editItem(@ModelAttribute("item") Item formItem) {
//		Optional<Item> item = itemService.findItemById(formItem.getItemid());
//
//		if (item.isEmpty()) {
//			return "redirect:/shop";
//		}
//
//		item.get().setName(formItem.getName());
//		item.get().setDescription(formItem.getDescription());
//		item.get().setPrice(formItem.getPrice());
//		item.get().setImagePath(formItem.getImagePath());
//		item.get().setImagePath2(formItem.getImagePath2());
//		item.get().setStock(formItem.getStock());
//		item.get().setSale(formItem.isSale());
//
//		itemService.saveItem(item.get());
//
//		return "redirect:/detail?id=" + item.get().getItemid();
//	}

	@GetMapping({ "/editItemForm", "/editItem" }) // 상품 수정 폼

	public String editItemForm(@RequestParam("itemid") Long itemid, Model model, Users users) {

		Optional<Item> item = itemService.findItemById(itemid);

		if (item.isEmpty()) {

			return "redirect:/shop";

		}

		model.addAttribute("users", users);

		model.addAttribute("item", item.get());

		Category[] category = Category.values();

		model.addAttribute("category", category);

		return "item/itemedit";

	}

	@PostMapping("/editItem") // 상품 수정

	public String editItem(@ModelAttribute("item") Item formItem, @RequestParam("imageFile") MultipartFile imageFile,

			@RequestParam("imageFile2") MultipartFile imageFile2) {

		return processItemForm(null, formItem, imageFile, imageFile2);

	}

	// 공통된 로직을 수행하는 메서드

	// 공통된 로직을 수행하는 메서드

	private String processItemForm(Model model, Item formItem, MultipartFile imageFile, MultipartFile imageFile2) {

		try {

			if (imageFile.isEmpty() || imageFile2.isEmpty()) {

				throw new IllegalArgumentException("파일을 선택해주세요.");

			}

			String uploadDir = "src/main/resources/static/images/imgfile/";

			// 이미지 업로드

			String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

			String imagePath = uploadDir + imageName;

			Files.copy(imageFile.getInputStream(), Paths.get(imagePath));

			formItem.setImagePath("/images/imgfile/" + imageName);

			String imageName2 = UUID.randomUUID().toString() + "_" + imageFile2.getOriginalFilename();

			String imagePath2 = uploadDir + imageName2;

			Files.copy(imageFile2.getInputStream(), Paths.get(imagePath2));

			formItem.setImagePath2("/images/imgfile/" + imageName2);

			itemService.saveItem(formItem); // 아이템 저장

			return (model != null) ? "redirect:/shop" : "redirect:/detail?id=" + formItem.getItemid();

		} catch (IOException e) {

			e.printStackTrace();

			return "errorPage";

		}

	}

}
