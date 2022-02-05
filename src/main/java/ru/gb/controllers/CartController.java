package ru.gb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.gb.api.product.api.ProductGateway;
import ru.gb.api.product.dto.ProductDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final Map<ProductDto, Integer> products = new ConcurrentHashMap<>();
    private final ProductGateway productGateway;
    private Boolean orderIsProcessed = false;

    @GetMapping
    public String showCart(Model model) {
        Map<String, Object> attributes = new ConcurrentHashMap<>();
        attributes.put("products", products);
        attributes.put("orderIsProcessed", orderIsProcessed);
        model.addAllAttributes(attributes);
        return "cart";
    }

    @GetMapping("/add")
    public String addProductToCart(Model model, @RequestParam(name = "id") Long id) {
        orderIsProcessed = false;
        model.addAttribute("orderIsProcessed", orderIsProcessed);
        if (products.size() == 0) {
            products.put(productGateway.getProduct(id).getBody(), 1);
        } else {
            for (ProductDto productDto : products.keySet()) {
                if (productDto.getId().equals(id)) {
                    products.merge(productDto, 1, Integer::sum);
                    return "redirect:/api/v1/product/all";
                }
            }
            products.put(productGateway.getProduct(id).getBody(), 1);
        }
        return "redirect:/api/v1/product/all";
    }

    @GetMapping("/delete")
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
        return "redirect:/api/v1/cart";
    }

    @GetMapping("/order")
    public String createOrder(Model model){
        orderIsProcessed = true;
        products.clear();
        Map<String, Object> attributes = new ConcurrentHashMap<>();
        attributes.put("products", products);
        attributes.put("orderIsProcessed", orderIsProcessed);
        model.addAllAttributes(attributes);
        return "cart";
    }
}
