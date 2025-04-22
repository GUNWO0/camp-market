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

    // 📄 물품 목록 (검색어 or 상태 필터)
    @GetMapping("")
    public String itemList(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) Item.Status status,
                           Model model) {
        List<Item> items;

        if (keyword != null && !keyword.isEmpty()) {
            items = itemRepository.findByTitleContaining(keyword);
        } else if (status != null) {
            items = itemRepository.findByStatus(status);
        } else {
            items = itemRepository.findAll();
        }

        model.addAttribute("items", items);
        return "item-list"; // templates/item-list.mustache
    }

    // ➕ 등록 폼 페이지
    @GetMapping("/new")
    public String newForm() {
        return "item-form"; // templates/item-form.mustache
    }

    // ✅ 등록 처리
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

    // 🔍 상세 보기
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 물품입니다."));
        model.addAttribute("item", item);
        return "item-detail"; // templates/item-detail.mustache
    }

    // ✏️ 수정 폼 페이지
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id).orElseThrow();

        // 필요한 필드를 하나씩 모델에 전달
        model.addAttribute("item", item);
        model.addAttribute("isOnSale", item.getStatus() == Item.Status.ON_SALE);
        model.addAttribute("isReserved", item.getStatus() == Item.Status.RESERVED);
        model.addAttribute("isSoldOut", item.getStatus() == Item.Status.SOLD_OUT);

        return "item-edit"; // templates/item-edit.mustache
    }

    // ✅ 수정 처리
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

        return "redirect:/items/" + id; // 상세보기 페이지로 이동
    }

    // 🗑️ 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        itemRepository.deleteById(id);
        return "redirect:/items";
    }
}
