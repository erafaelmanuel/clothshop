package com.clothshop.client.web.service;

import com.clothshop.client.web.domain.RestTemplateSupport;
import com.clothshop.client.web.domain.User;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class UserService implements RestTemplateSupport {

    private EurekaClient client;

    @Autowired
    public UserService(EurekaClient client) {
        this.client = client;
    }

    public PagedResources<User> findAll() {
        return findAll(null);
    }

    public PagedResources<User> findAll(String search) {
        return findAll(search, 0, 0, null);
    }

    public PagedResources<User> findAll(String search, int page, int size, String sort) {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("account-service", false);
        final String url = instanceInfo.getHomePageUrl().concat("/users/all");

        return getRestTemplate().exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference
                        <PagedResources<User>>() {
                }).getBody();
    }


    public Resource<User> findById(String userId) {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("account-service", false);
        final String url = instanceInfo.getHomePageUrl().concat("/users/").concat(userId);

        return getRestTemplate().exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<Resource<User>>() {
                }).getBody();
    }
}
