package com.noteif.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.noteif.domain.Application;
import com.noteif.domain.XmppUser;
import com.noteif.repository.ApplicationRespository;
import com.noteif.service.XmppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    ApplicationRespository applicationRespository;
    @Autowired
    XmppService xmppService;

    @RequestMapping(value = "users/{providerId}/application/{applicationId}", method = RequestMethod.GET)
    public Application getUserApplication(@PathVariable("providerId") String providerId, @PathVariable("applicationId") UUID applicationId) {
        return applicationRespository.findOne(applicationId);
    }

    @RequestMapping(value = "users/applications", method = RequestMethod.POST)
    public Application saveUserApplication(@RequestBody Application application) {
        List<XmppUser> newXmppUsers = application.getXmppUsers()
                .stream()
                .filter(xmppUser -> xmppUser.getId() == null)
                .collect(Collectors.toList());
        xmppService.createUsers(newXmppUsers, application.getId());
        return applicationRespository.save(application);
    }

    @RequestMapping(value = "users/applications/{applicationId}/{username}", method = RequestMethod.GET)
    public Map<String, String> getApplicationUser(@PathVariable("applicationId") UUID applicationId, @PathVariable("username") String username) {
        Application application = applicationRespository.findOne(applicationId);
        Map<String,String> userMap = new HashMap<>();
        XmppUser xmppUser;
        try {
            xmppUser = application.getXmppUsers().stream().filter(user -> user.getUsername().equals(username)).findFirst().get();
        } catch(NoSuchElementException e) {
            xmppUser = new XmppUser(username);
            xmppService.createUsers(ImmutableList.of(xmppUser), applicationId);
            application.getXmppUsers().add(xmppUser);
            applicationRespository.save(application);
        }
        userMap.put("username", applicationId.toString() + "-" + xmppUser.getUsername());
        userMap.put("password", xmppUser.getPassword());
        return userMap;
    }

}
