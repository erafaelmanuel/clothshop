package com.clothshop.account.util;

import com.clothshop.account.domain.EntitySpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchingAndPagingUtil<T> {

    private EntitySpecificationBuilder<T> builder = new EntitySpecificationBuilder<>();

    private Pageable pageable;

    public SearchingAndPagingUtil(String search, Integer page, Integer size, String sort) {

        final int tempPage = (page != null && page > 0 ? page - 1 : 0);
        final int tempSize = (size != null ? size : 20);
        final String tempSort = !StringUtils.isEmpty(sort) ? sort : "name";

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
        pageable = PageRequest.of(tempPage, tempSize, Sort.by(tempSort));
    }

    public Specification<T> getSpecification() {
        return builder.build();
    }

    public Pageable getPageable() {
        return pageable;
    }

    public int getNumber() {
        return pageable.getPageNumber() + 1;
    }

    public int getSize() {
        return pageable.getPageSize();
    }
}
