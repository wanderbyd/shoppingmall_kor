package com.shoppingmall.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.users.UserService;
import com.shoppingmall.users.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

  @Autowired
  private UserService userService;

  @GetMapping("/login")
  public String loginForm() {
      return "user/login";
  }



  @PostMapping("/login")
  public String loginSubmit(@RequestParam String email, @RequestParam String password, HttpSession session) {
      // 이메일을 통해 사용자 찾기
      Users user = userService.findUserByEmail(email).orElse(null);
      if (user != null) {
          // 입력된 비밀번호 해싱 후 기존 해시된 비밀번호와 비교
          String hashedPassword = userService.hashPassword(password);
          if (hashedPassword != null && hashedPassword.equals(user.getHashedpassword())) {
              // 로그인 성공
              // 세션에 사용자 정보 저장
              session.setAttribute("loggedInUser", user);
              
              if (user.getEmail().equals("admin@admin.com")) {
					session.setAttribute("isAdmin", "true");
				} else {
					session.setAttribute("isAdmin", "false");
				}

              return "redirect:/"; // 대시보드 페이지로 이동
          }
      }
      return "user/login"; // 실패 시 다시 로그인 폼으로 이동
  }
  
  @GetMapping("/dashboard")
  public String dashboard(HttpSession session, Model model) {
      Users loggedInUser = (Users) session.getAttribute("loggedInUser");

      if (loggedInUser != null) {
          // 로그인된 사용자 정보를 모델에 추가하여 JSP에서 사용할 수 있도록 함
          model.addAttribute("loggedInUser", loggedInUser);
          return "main";
      } else {
          return "redirect:/login"; // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
      }
  }
  
  @GetMapping("/logout")
  public String logout(HttpServletRequest request) {
      HttpSession session = request.getSession(false);
      if (session != null) {
          session.invalidate(); // Invalidates the session
      }
      return "redirect:/"; // Redirect to the home page or any desired location after logout
  }



}