package com.example.campmarket.item;

import com.example.campmarket.user.User;
import com.example.campmarket.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @GetMapping("")
    public String itemList(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "item-list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "item-form";
    }

    @PostMapping("")
    public String createItem(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam int price,
                             @RequestParam Item.Status status) {
        User user = userRepository.findAll().get(0);
        itemRepository.save(Item.builder()
                .title(title)
                .description(description)
                .price(price)
                .status(status)
                .seller(user)
                .build());
        return "redirect:/items";
    }
}
