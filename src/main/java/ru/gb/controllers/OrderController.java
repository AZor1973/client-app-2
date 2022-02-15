package ru.gb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.api.order.api.OrderGateway;
import ru.gb.api.order.dto.OrderDto;
import ru.gb.api.product.api.ProductGateway;
import ru.gb.api.product.dto.ProductDto;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/shop/order")
@RequiredArgsConstructor
public class OrderController {

    private final Map<ProductDto, Integer> products = new ConcurrentHashMap<>();
    private final ProductGateway productGateway;
    private final OrderGateway orderGateway;
    private Boolean orderIsProcessed = false;

    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("products", products);
        model.addAttribute("orderIsProcessed", orderIsProcessed);
        return "cart";
    }

    @GetMapping("/cart/add")
    public String addProductToCart(Model model, @RequestParam(name = "id") Long id) {
        orderIsProcessed = false;
        model.addAttribute("orderIsProcessed", orderIsProcessed);
        if (products.size() == 0) {
            products.put(productGateway.getProduct(id).getBody(), 1);
        } else {
            for (ProductDto productDto : products.keySet()) {
                if (productDto.getId().equals(id)) {
                    products.merge(productDto, 1, Integer::sum);
                    return "redirect:/shop/product/all";
                }
            }
            products.put(productGateway.getProduct(id).getBody(), 1);
        }
        return "redirect:/shop/product/all";
    }

    @DeleteMapping("/cart/delete")
    public String deleteProductFromCart(@RequestParam(name = "id") Long id) {
        for (ProductDto productDto : products.keySet()) {
            if (productDto.getId().equals(id)) {
                if (products.get(productDto) == 1) {
                    products.remove(productDto);
                } else {
                    products.merge(productDto, -1, Integer::sum);
                }
            }
        }
        return "redirect:/shop/cart";
    }

    @GetMapping
    public String createOrder(Model model){
        model.addAttribute("order", new OrderDto());
        orderIsProcessed = true;
        return "order-form";
    }

    @PostMapping("/create")
    public String saveOrder(Model model, OrderDto orderDto) {
        orderDto.setProducts(products.keySet());
        orderGateway.handlePost(orderDto);
        model.addAttribute("orders", orderGateway.getOrderList());
        products.clear();
        return "order-list";
    }

    @GetMapping("/all")
    public String showOrderList(Model model) {
        model.addAttribute("orders", orderGateway.getOrderList());
        return "order-list";
    }
}
