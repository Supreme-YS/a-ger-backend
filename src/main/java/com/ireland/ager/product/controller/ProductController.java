package com.ireland.ager.product.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
public class ProductController {

    @GetMapping(value = "product")
    public String getProduct(Model model) {

    }
}
