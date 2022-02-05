package ru.gb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("products", new ArrayList<>());
        return "cart";
    }
}
