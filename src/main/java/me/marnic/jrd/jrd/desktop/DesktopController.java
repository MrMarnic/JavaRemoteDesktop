package me.marnic.jrd.jrd.desktop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Copyright (c) 02.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Controller
public class DesktopController {

    @GetMapping("/desktop")
    public String desktop(Model model, @RequestParam int monIndex) {
        model.addAttribute("openedpath",new OpenedPath(monIndex));

        return "desktop";
    }
}
