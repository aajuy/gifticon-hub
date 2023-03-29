package com.hub.gifticon.web;

import com.hub.gifticon.domain.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginUser", required = false) User loginUser,
                       Model model) {
        // 세션에 회원 데이터가 없으면
        if (loginUser == null) {
            return "home";
        }

        // 세션에 회원 데이터가 있으면
        model.addAttribute("user", loginUser);
        return "loginHome";
    }
}
