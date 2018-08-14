package com.clothshop.client.web.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface BaseService {

    default HttpMessageConverter<?> getHalConverter() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final MappingJackson2HttpMessageConverter halConverter = new
                TypeConstrainedMappingJackson2HttpMessageConverter(ResourceSupport.class);

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new Jackson2HalModule());

        halConverter.setSupportedMediaTypes(Collections.singletonList(MediaTypes.HAL_JSON));
        halConverter.setObjectMapper(objectMapper);

        return halConverter;
    }

    default RestTemplate getRestTemplate() {
        final List<HttpMessageConverter<?>> newConverters = new ArrayList<>();

        final RestTemplate restTemplate = new RestTemplate();

        newConverters.add(getHalConverter());
        newConverters.addAll(restTemplate.getMessageConverters());

        restTemplate.setMessageConverters(newConverters);

        return restTemplate;
    }
}
