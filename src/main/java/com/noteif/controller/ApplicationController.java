package com.noteif.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.noteif.domain.Application;
import com.noteif.domain.XmppUser;
import com.noteif.repository.ApplicationRespository;
import com.noteif.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    ApplicationRespository applicationRespository;

    @RequestMapping(value = "users/{providerId}/application/{applicationId}", method = RequestMethod.GET)
    public Application getUserApplication(@PathVariable("providerId") String providerId, @PathVariable("applicationId") UUID applicationId) {
        return applicationRespository.findOne(applicationId);
    }

    @RequestMapping(value = "users/applications", method = RequestMethod.POST)
    public Application saveUserApplication(@RequestBody Application application) {
        List<XmppUser> newXmppUsers = application.getXmppUsers().stream().filter(xmppUser -> xmppUser.getId() != null).collect(Collectors.toList());
        return applicationRespository.save(application);
    }

}
