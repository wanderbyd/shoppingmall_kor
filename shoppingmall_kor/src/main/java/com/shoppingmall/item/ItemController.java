package com.shoppingmall.item;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingmall.qna.Qna;
import com.shoppingmall.qna.QnaService;

import com.shoppingmall.users.UserService;
import com.shoppingmall.users.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private QnaService qnaService;

	@GetMapping("/shop")
	public String shopPage(Model model, @RequestParam Optional<String> category,
			@RequestParam("min") Optional<Long> min, @RequestParam("max") Optional<Long> max,
			@RequestParam("keyword") Optional<String> keyword, Users users) {

		Category[] categories = Category.values();
		model.addAttribute("category", categories);

		List<Item> items;

		if (max.isPresent() && min.isPresent()) {
			items = itemService.findAllItems().stream()
					.filter(item -> item.getPrice() <= max.get() && item.getPrice() >= min.get())
					.collect(Collectors.toList());
		} else if (keyword.isPresent()) {
			// Search items based on keyword
			items = itemService.searchItemsByKeyword(keyword.get());

			// Add the selected category to the model to maintain it in the view
			// model.addAttribute("selectedCategory", category.orElse(null));
		} else if (category.isPresent()) {
			items = itemService.findAllItems().stream()
					.filter(item -> item.getCategory().toString().equals(category.get())).collect(Collectors.toList());

		} else {
			// Show all items if no specific filter is applied
			items = itemService.findAllItems();
		}

		model.addAttribute("items", items);
		model.addAttribute("users", users);

		return "item/itemmain";
	}


// 사진
	@GetMapping("/upload-form")
	public String showUploadForm(Model model) {
		model.addAttribute("item", new Item());
		return "upload-form";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("imageFile") MultipartFile files) {
		try {
			if (files.isEmpty()) {
				throw new IllegalArgumentException("파일을 선택해주세요.");
			}

			String sourceFileName = files.getOriginalFilename();
			String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();

			if (!isValidFileExtension(sourceFileNameExtension)) {
				throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
			}

			String sourceFileNameWithoutExtension = FilenameUtils.removeExtension(sourceFileName);

			String rootPath = "D:\\spring_workspace\\shoppingmall4\\src\\main\\resources\\static\\images\\imgfile\\";
			String relativePath = "upload";
			String fileUrl = rootPath + "/" + relativePath + "/";

			String destinationFileName = generateRandomFileName(sourceFileNameExtension);
			File destinationFile = new File(fileUrl + destinationFileName);

			destinationFile.getParentFile().mkdirs();
			files.transferTo(destinationFile);

			Item item = new Item();
			item.setFilename(destinationFileName);
			item.setFileOriName(sourceFileNameWithoutExtension);
			item.setFileUrl(fileUrl);

			itemService.createItem(item);

			return "redirect:/upload-form";
		} catch (IOException e) {
			e.printStackTrace();
			return "errorPage";
		}
	}

	@PostMapping("/create")
	@Transactional
	public String uploadFile1(@RequestParam("imageFile") MultipartFile files) {
		try {
			if (files.isEmpty()) {
				throw new IllegalArgumentException("파일을 선택해주세요.");
			}

			String sourceFileName = files.getOriginalFilename();
			String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();

			if (!isValidFileExtension(sourceFileNameExtension)) {
				throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
			}

			String sourceFileNameWithoutExtension = FilenameUtils.removeExtension(sourceFileName);

			String rootPath = "D:\\spring_workspace\\shoppingmall4\\src\\main\\resources\\static\\images\\imgfile\\";
			String relativePath = "upload";
			String fileUrl = rootPath + "/" + relativePath + "/";

			String destinationFileName = generateRandomFileName(sourceFileNameExtension);
			File destinationFile = new File(fileUrl + destinationFileName);

			destinationFile.getParentFile().mkdirs();
			files.transferTo(destinationFile);

			Item item = new Item();
			item.setFilename(destinationFileName);
			item.setFileOriName(sourceFileNameWithoutExtension);
			item.setFileUrl(fileUrl);

			itemService.createItem(item);

			return "redirect:/upload-form";
		} catch (IOException e) {
			e.printStackTrace();
			return "errorPage";
		}

	}

	private String generateRandomFileName(String extension) {
		String randomName = RandomStringUtils.randomAlphabetic(32);
		return randomName + "." + extension;
	}

	private boolean isValidFileExtension(String fileExtension) {
		List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");
		return allowedExtensions.contains(fileExtension);
	}

	@GetMapping("/search")
	public String searchItemsByKeyword(@RequestParam(name = "keyword", required = false) String keyword, Model model,
			@RequestParam Optional<String> category) {
		if (keyword != null) {
			List<Item> searchResults = itemService.searchItemsByKeyword(keyword);
			model.addAttribute("items", searchResults);
			model.addAttribute("searchKeyword", keyword); // 검색 키워드를 모델에 추가
			Category[] categories = Category.values();
			model.addAttribute("category", categories);
		}
		return "item/itemmain";
	}

	//////////////////////////////// Q&A ///////////////////////////////////////
	public static Item item;
	public static List<Qna> qnaList;

	@GetMapping("/detail")
	public String showDetail(@RequestParam("id") Long itemid, Model model) {
		Optional<Item> optionalItem = itemService.findItemById(itemid);

		Item item = null;
		if (optionalItem.isPresent()) {
			item = optionalItem.get();
			this.item = item;
			model.addAttribute("item", item);

			try {
				List<Qna> qnaList = qnaService.getQnasByItemId(itemid);
				this.qnaList = qnaList;
				model.addAttribute("qnaList", qnaList);
				System.out.println("자료가 있거던! ");
				for (Qna x : qnaList) {
					System.out.println("?????:" + x);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// return "item/itemdetail_list";
		return "item/itemdetail";

	}

	@PostMapping("/submit")
	public String submitQuestion(@RequestParam("question") String questionContent, @RequestParam("usersid") Long userId,
			@RequestParam("itemid") Long itemId, Model model) {

		Qna qna = new Qna();
		qna.setQuestion(questionContent);
		qna.setUsersid(userId);
		qna.setItemid(itemId);

		qnaService.saveQna(qna);

		// System.out.println("Saved Qna: " + qna);

		model.addAttribute("qna", qna);

		return "redirect:/detail?id=" + itemId;
	}

	@PostMapping("/answer")
	public ResponseEntity<String> submitAnswer(@RequestParam("qnaid") Long qnaid,
			@RequestParam("answer") String answer) {
		Qna savedQna = qnaService.saveAnswer(qnaid, answer);

		if (savedQna != null) {
			// 저장된 Qna 객체에서 답변 내용을 가져와 응답으로 전송
			return ResponseEntity.ok(savedQna.getAnswer());
		} else {
			return ResponseEntity.badRequest().body("답변 저장에 실패했습니다.");
		}
	}

	@PostMapping("/delete/{qnaid}")
	public ResponseEntity<String> deleteQnA(@PathVariable Long qnaid, HttpSession session) {
		Users loggedInUser = (Users) session.getAttribute("loggedInUser");
		String isAdmin = (String) session.getAttribute("isAdmin");

		Optional<Qna> qnaOptional = qnaService.getQnaById(qnaid);

		if (qnaOptional.isPresent()) {
			Qna qna = qnaOptional.get();

			// 본인이 작성한 글이거나 관리자인 경우 삭제 가능
			if (qna.getUsersid().equals(loggedInUser.getUsersid()) || isAdmin.equals("true")) {
				qnaService.deleteQnaById(qnaid);
				return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity<>("요청한 QnA가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/edit")
	public String editQnA(@RequestParam("qnaid") Long qnaid, Model model, HttpServletRequest request,
			HttpSession session) {
		Optional<Qna> qnaOptional = qnaService.getQnaById(qnaid);
		Users loggedInUser = (Users) session.getAttribute("loggedInUser");
		String isAdmin = (String) session.getAttribute("isAdmin");

		if (qnaOptional.isPresent()) {
			Qna qna = qnaOptional.get();

			// 사용자가 질문 작성자이거나 관리자인 경우 수정 가능
			if (loggedInUser != null
					&& (qna.getUsersid().equals(loggedInUser.getUsersid()) || isAdmin.equals("true"))) {
				model.addAttribute("qna", qna);
				String referer = request.getHeader("Referer");
				model.addAttribute("referer", referer);
				return "item/qnaedit";
			}
		}

		model.addAttribute("errorMessage", "수정 권한이 없습니다.");
		return "item/error";
	}

	@PostMapping("/update")
	public String updateQnA(@ModelAttribute("qna") Qna updatedQna, @RequestParam("referer") String referer,
			RedirectAttributes attributes, HttpSession session) {
		Users loggedInUser = (Users) session.getAttribute("loggedInUser");
		String isAdmin = (String) session.getAttribute("isAdmin");
		Optional<Qna> qnaOptional = qnaService.getQnaById(updatedQna.getQnaid());

		if (qnaOptional.isPresent()) {
			Qna qna = qnaOptional.get();

			// 사용자가 질문 작성자이거나 관리자인 경우에만 수정 허용
			if ((loggedInUser != null && qna.getUsersid().equals(loggedInUser.getUsersid()))
					|| isAdmin.equals("true")) {
				qna.setQuestion(updatedQna.getQuestion());
				qnaService.updateQna(qna);

				attributes.addFlashAttribute("successMessage", "수정이 성공적으로 완료되었습니다.");
			} else {
				attributes.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
			}
		} else {
			attributes.addFlashAttribute("errorMessage", "요청한 QnA가 존재하지 않습니다.");
		}
		return "redirect:" + referer;
	}
}
