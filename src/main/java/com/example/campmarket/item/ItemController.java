package com.example.campmarket.item;

import com.example.campmarket.user.User;
import com.example.campmarket.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @GetMapping("/dummy")
    public String saveDummyItem() {
        User user = userRepository.save(User.builder()
                .email("test@school.ac.kr")
                .nickname("홍길동")
                .password("1234")
                .build());

        itemRepository.save(Item.builder()
                .title("중고 자바 책")
                .description("1학년 때 쓰던 책입니다.")
                .price(5000)
                .status(Item.Status.ON_SALE)
                .seller(user)
                .build());

        return "ok";
    }
}
