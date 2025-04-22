package com.example.campmarket.item;


import com.example.campmarket.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int price;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ON_SALE, RESERVED, SOLD_OUT
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User seller;

}
