package ru.gb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.api.order.api.OrderGateway;
import ru.gb.api.order.dto.OrderDto;

@Controller
@RequestMapping("/shop/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderGateway orderGateway;

    @GetMapping("/all")
    public String showOrderList(Model model) {
        model.addAttribute("orders", orderGateway.getOrderList());
        return "order-list";
    }
}
