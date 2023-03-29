package com.hub.gifticon.web.user;

import com.hub.gifticon.domain.user.User;
import com.hub.gifticon.domain.user.UserService;
import com.hub.gifticon.web.user.form.LoginForm;
import com.hub.gifticon.web.user.form.addUserForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/add")
    public String addUserForm(@ModelAttribute("form") addUserForm form) {
        return "users/addUserForm";
    }

    @PostMapping("/add")
    public String addUser(@Validated @ModelAttribute("form") addUserForm form,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/addUserForm";
        }

        User addedUser = userService.addUser(form.getUserId(), form.getPassword());
        if (addedUser == null) {
            bindingResult.reject("addUserFail", "이미 존재하는 아이디입니다.");
            return "users/addUserForm";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("form") LoginForm form) {
        return "users/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("form") LoginForm form,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        // 로그인 실패
        if (bindingResult.hasErrors()) {
            return "users/loginForm";
        }

        User loginUser = userService.login(form.getUserId(), form.getPassword());

        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/loginForm";
        }

        // 로그인 성공
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);
        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
