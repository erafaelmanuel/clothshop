package com.clothshop.client.web.service;

import com.clothshop.client.web.domain.User;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService implements BaseService {

    private EurekaClient client;
    private RestTemplateBuilder builder;

    @Autowired
    public UserService(EurekaClient client, RestTemplateBuilder builder) {
        this.client = client;
        this.builder = builder;
    }

    public User findById(String userId) {
        final RestTemplate restTemplate = builder.build();
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("account-service", false);
        final String baseUrl = instanceInfo.getHomePageUrl();

        restTemplate.getMessageConverters().add(getHalConverter());
        return restTemplate.exchange(baseUrl.concat("/users/").concat(userId), HttpMethod.GET, null,
                new ParameterizedTypeReference<Resource<User>>() {}).getBody().getContent();
    }
}
