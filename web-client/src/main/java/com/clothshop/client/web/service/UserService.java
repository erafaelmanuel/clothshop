package com.clothshop.client.web.service;

import com.clothshop.client.web.domain.RestTemplateSupport;
import com.clothshop.client.web.domain.User;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserService implements RestTemplateSupport {

    private EurekaClient client;
    private RestTemplateBuilder builder;

    @Autowired
    public UserService(EurekaClient client, RestTemplateBuilder builder) {
        this.client = client;
        this.builder = builder;
    }
    public PagedResources<User> findAll() {
        return findAll("", 0, 0, "");
    }

    public PagedResources<User> findAll(String search, int page, int size, String sort) {
        final RestTemplate restTemplate = builder.build();
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("account-service", false);
        final UriComponentsBuilder b = UriComponentsBuilder.fromHttpUrl(
                instanceInfo.getHomePageUrl().concat("/users/all"));

        if (!StringUtils.isEmpty(search)) {
            b.queryParam("search", search);
        }
        if (page > 0) {
            b.queryParam("page", page);
        }
        if (size > 0) {
            b.queryParam("size", size);
        }
        if (!StringUtils.isEmpty(sort)) {
            b.queryParam("sort", sort);
        }

        restTemplate.getMessageConverters().add(getHalConverter());
        return restTemplate.exchange(
                b.toString(), HttpMethod.GET, null, new ParameterizedTypeReference
                        <PagedResources<User>>() {
                }).getBody();
    }


    public Resource<User> findById(String userId) {
        final RestTemplate restTemplate = builder.build();
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("account-service", false);
        final String baseUrl = instanceInfo.getHomePageUrl();

        restTemplate.getMessageConverters().add(getHalConverter());
        return restTemplate.exchange(baseUrl.concat("/users/").concat(userId), HttpMethod.GET, null,
                new ParameterizedTypeReference<Resource<User>>() {
                }).getBody();
    }
}
