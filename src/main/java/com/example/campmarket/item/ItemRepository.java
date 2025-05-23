package com.example.campmarket.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTitleContaining(String keyword);

    List<Item> findByStatus(Item.Status status);
}