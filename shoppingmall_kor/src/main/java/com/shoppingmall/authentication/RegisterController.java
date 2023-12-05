package com.shoppingmall.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shoppingmall.users.UserService;
import com.shoppingmall.users.Users;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new Users());
        return "user/register";
    }


    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Users user) {
        if (userService.doesEmailAlreadyExists(user.getEmail())) {
            // 처리할 예외 상황에 대한 로직 추가 (이미 사용 중인 이메일일 경우)
            // 여기에 이미 등록된 이메일에 대한 처리 로직을 추가하세요.
            // 예: 오류 메시지 표시 또는 다른 처리 방법 선택
            return "user/register"; // 실패 시 다시 회원가입 폼으로 이동
        } else {
            if (user.getHashedpassword() == null || user.getHashedpassword().isEmpty()) {
                // 처리할 예외 상황에 대한 로직 추가 (비어있는 비밀번호)
                // 여기에 비밀번호가 비어있는 경우의 처리 로직을 추가하세요.
                // 예: 오류 메시지 표시 또는 다른 처리 방법 선택
                return "user/register"; // 실패 시 다시 회원가입 폼으로 이동
            } else {
                String hashedPassword = userService.hashPassword(user.getHashedpassword());
                user.setHashedpassword(hashedPassword);
                userService.createUser2(user);
                return "redirect:/login"; // 회원가입 성공 시 로그인 페이지로 이동
            }
        }
    }
}