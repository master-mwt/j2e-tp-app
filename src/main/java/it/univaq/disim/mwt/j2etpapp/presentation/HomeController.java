package it.univaq.disim.mwt.j2etpapp.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("")
    public String root() {
        return "index";
    }
}
