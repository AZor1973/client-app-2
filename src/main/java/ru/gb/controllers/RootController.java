package ru.gb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping
    public String getRootPath(){
        return "redirect:/shop/product/all";
    }
}
