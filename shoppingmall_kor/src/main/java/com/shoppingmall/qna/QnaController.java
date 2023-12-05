package com.shoppingmall.qna;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.item.ItemService;
import com.shoppingmall.users.UserService;
import com.shoppingmall.users.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class QnaController {

	@Autowired
	private QnaService qnaService;
	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;

	@GetMapping("/check-login-status")
	public ResponseEntity<String> checkLoginStatus(Model model, HttpServletRequest request, Long itemIdValue) {
		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("loggedInUser") != null) {
			// 세션이 존재하고 로그인 정보가 있다면 사용자 정보 가져오기
			Users loggedInUser = (Users) session.getAttribute("loggedInUser");

			// qnaIdValue와 itemIdValue를 세션에 설정
			// session.setAttribute("qnaid", qnaIdValue);
			session.setAttribute("itemid", itemIdValue);

			// 사용자 정보에서 필요한 데이터 추출
			Long usersId = loggedInUser.getUsersid();

			// 모델에 값 추가
			model.addAttribute("usersid", usersId);
			System.out.println("usersId:" + usersId);
			return ResponseEntity.ok().body("로그인됨");
		} else {
			// 로그인되지 않은 경우 처리
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않음");
		}
	}

	@PostMapping("/popup")
	public String showPopup(@RequestParam("itemid") Long itemId, Model model, HttpServletRequest request) {
		// Long itemIdValue = itemId;
		Long itemIdValue = Long.parseLong(request.getParameter("itemid"));

		// checkLoginStatus 메서드에서 모델에 필요한 값들을 추가합니다
		ResponseEntity<String> loginStatusResponse = checkLoginStatus(model, request, itemIdValue);
		if (loginStatusResponse.getStatusCode() == HttpStatus.OK) {
			model.addAttribute("itemIdValue", itemIdValue);
			System.out.println("itemId: " + itemIdValue);
			return "item/popup"; // 팝업 템플릿으로 이동
		} else {
			return "redirect:/login"; // 로그인 페이지로 이동하거나 다른 처리 수행
		}
	}

}
