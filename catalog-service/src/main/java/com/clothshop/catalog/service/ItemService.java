package com.clothshop.catalog.service;

import com.clothshop.catalog.data.entity.Item;
import com.clothshop.catalog.data.repository.ItemRepository;
import com.clothshop.catalog.exception.EntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private ItemRepository itemRepo;

    public ItemService(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    public List<Item> findAll() {
        final List<Item> items = new ArrayList<>();

        if (itemRepo.findAll() != null) {
            itemRepo.findAll().forEach(items::add);
        }
        return items;
    }

    public Page<Item> findAll(Specification<Item> specification, Pageable pageable) {
        return itemRepo.findAll(specification, pageable);
    }

    public Item findById(@NotNull String id) {
        return itemRepo.findById(id).orElseThrow(() -> new EntityException("No item found"));
    }
}
