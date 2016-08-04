package com.noteif.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AdminUIController {
    @RequestMapping(value = "adminUi/xmppInfo/{username}/{applicationId}", method = RequestMethod.GET)
    public Object getXmppInfo(@PathVariable("username") String username, @PathVariable("applicationId") String applicationId) {
        return new RestTemplate().exchange("localhost:8080users/applications/" + applicationId + "/" + username,
                HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), Object.class);
    }
}
