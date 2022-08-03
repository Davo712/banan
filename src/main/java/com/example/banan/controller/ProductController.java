package com.example.banan.controller;

import com.example.banan.model.Product;
import com.example.banan.model.User;
import com.example.banan.repository.ProductRepository;
import com.example.banan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public UserRepository userRepository;

    @GetMapping("/getAllProducts")
    public String getAllProducts(Model model) {
        List products = productRepository.findAll();
        model.addAttribute("products", products);
        return null;
    }

    @GetMapping("/getProducts/{username}")
    public String getProducts(@PathVariable String username, Principal principal, Model model) {
        User user = userRepository.findByUsername(username);
        List products = user.getProducts();
        model.addAttribute("products", products);
        return null;

    }

    @GetMapping("/getProducts/my")
    public String myProducts(Principal principal, Model model) throws IOException {
        model.addAttribute("products", userRepository.findByUsername(principal.getName()).getProducts());
        return null;
    }

    @PostMapping("/addProduct")
    public String addProduct(Principal principal, Product product) {
        User user = userRepository.findByUsername(principal.getName());
        product.setSellerName(user.getUsername());
        user.getProducts().add(product);
        userRepository.save(user);
        productRepository.save(product);
        return null;
    }

    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable String id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Long productId = Long.getLong(id);
        List products = user.getProducts();
        Product product = productRepository.getById(productId);
        if (product != null) {
            products.remove(product);
            user.setProducts(products);
            userRepository.save(user);
            productRepository.delete(product);
        }
        return null;
    }

}
