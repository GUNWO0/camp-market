package com.example.campmarket.item;

import com.example.campmarket.user.User;
import com.example.campmarket.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new IllegalStateException("사용자가 없습니다. 최소 한 명의 사용자가 필요합니다.");
        }
        User user = users.get(0);
        itemRepository.save(Item.builder()
                .title(title)
                .description(description)
                .price(price)
                .status(status)
                .seller(user)
                .build());
        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id).orElseThrow();
        model.addAttribute("item", item);
        return "item-detail"; // item-detail.mustache
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id).orElseThrow();
        model.addAttribute("item", item);
        return "item-edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String description,
                         @RequestParam int price,
                         @RequestParam Item.Status status) {

        Item item = itemRepository.findById(id).orElseThrow();
        item.setTitle(title);
        item.setDescription(description);
        item.setPrice(price);
        item.setStatus(status);
        itemRepository.save(item);

        return "redirect:/items/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        itemRepository.deleteById(id);
        return "redirect:/items";
    }
}
