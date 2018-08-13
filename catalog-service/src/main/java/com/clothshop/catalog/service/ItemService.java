package com.clothshop.catalog.service;

import com.clothshop.catalog.data.entity.Category;
import com.clothshop.catalog.data.entity.Item;
import com.clothshop.catalog.data.repository.ItemRepository;
import com.clothshop.catalog.exception.EntityException;
import com.clothshop.catalog.util.UUIDGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public Page<Item> findByCategoryId(@NotNull List<String> categoryId, Pageable pageable) {
        return itemRepo.findByCategoryId(categoryId, pageable);
    }

    public Page<Category> findCategoriesById(String itemId, Pageable pageable) {
        return itemRepo.findCategoriesById(itemId, pageable);
    }

    public void save(@NotNull Item item) {
        if (StringUtils.isEmpty(item.getName())) {
            throw new EntityException("Property name is required!");
        }
        if (item.getPrice() < 0) {
            throw new EntityException("Property price only accepts non-negative numbers");
        }
        if (StringUtils.isEmpty(item.getId())) {
            item.setId(UUIDGenerator.randomUUID());
        }
        itemRepo.save(item);
    }

    public void delete(Item item) {
        itemRepo.delete(item);
    }
}
