package com.clothshop.catalog.util;

import com.clothshop.catalog.domain.EntitySpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchingAndPagingUtil<T> {

    private String search;
    private Integer page;
    private Integer size;
    private String sort;

    public SearchingAndPagingUtil(String search, Integer page, Integer size, String sort) {
        this.search = search;
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public Specification<T> buildSpecification() {
        final EntitySpecificationBuilder<T> builder = new EntitySpecificationBuilder<>();

        if (!StringUtils.isEmpty(search)) {
            final Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            final Matcher matcher = pattern.matcher(search.concat(","));

            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            if (builder.getParamSize() == 0) {
                builder.with("name", ":", search);
            }
        }
        return builder.build();
    }

    public Pageable buildPageable() {
        final int tempPage = (page != null && page > 0 ? page - 1 : 0);
        final int tempSize = (size != null ? size : 20);
        final String tempSort = !StringUtils.isEmpty(sort) ? sort : "name";

        return PageRequest.of(tempPage, tempSize, Sort.by(tempSort));
    }

    public int getNumber() {
        return (page != null && page > 0 ? page - 1 : 0) + 1;
    }

    public int getSize() {
        return (size != null ? size : 20);
    }
}
