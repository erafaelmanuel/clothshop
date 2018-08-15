package com.clothshop.catalogservice.service;

import com.clothshop.catalogservice.data.entity.Category;
import com.clothshop.catalogservice.data.entity.Item;
import com.clothshop.catalogservice.data.repository.ItemRepository;
import com.clothshop.catalogservice.exception.EntityConstraintViolationException;
import com.clothshop.catalogservice.exception.ItemNotFoundException;
import com.clothshop.catalogservice.util.UUIDGenerator;
import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    private MessageSource messageSource;

    @Autowired
    public ItemService(ItemRepository itemRepo, MessageSource messageSource) {
        this.itemRepo = itemRepo;
        this.messageSource = messageSource;
    }

    public List<Item> findAll() {
        final List<Item> items = new ArrayList<>();

        if (itemRepo.findAll() != null) {
            itemRepo.findAll().forEach(items::add);
        }
        return items;
    }

    public Page<Item> findAll(@Nullable Specification<Item> specification, @Nullable Pageable pageable) {
        return itemRepo.findAll(specification, pageable);
    }

    public Item findById(@NotNull String id) {
        return itemRepo.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    public Page<Item> findByCategoryId(@NotNull List<String> categoryId, @Nullable Pageable pageable) {
        return itemRepo.findByCategoryId(categoryId, pageable);
    }

    public Page<Category> findCategoriesById(String itemId, @Nullable Pageable pageable) {
        return itemRepo.findCategoriesById(itemId, pageable);
    }

    public void save(@NotNull Item item) {
        if (StringUtils.isEmpty(item.getName())) {
            throw new EntityConstraintViolationException(
                    messageSource.getMessage("error_message.name.required", null, null));
        }
        if (item.getPrice() < 0) {
            throw new EntityConstraintViolationException(
                    messageSource.getMessage("error_message.price.non-negative", null, null));
        }
        if (StringUtils.isEmpty(item.getId())) {
            item.setId(UUIDGenerator.randomUUID());
        }
        itemRepo.save(item);
    }

    public void delete(@NotNull Item item) {
        itemRepo.delete(item);
    }
}
