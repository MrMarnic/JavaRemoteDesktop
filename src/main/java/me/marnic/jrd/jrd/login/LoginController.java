package me.marnic.jrd.jrd.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Copyright (c) 30.07.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}

