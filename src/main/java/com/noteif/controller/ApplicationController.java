package com.noteif.controller;

import java.util.UUID;

import com.noteif.domain.Application;
import com.noteif.repository.ApplicationRespository;
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

    @RequestMapping(value = "users/{userId}/application/{applicationId}", method = RequestMethod.GET)
    public Application getUserApplication(@PathVariable("userId") UUID userId, @PathVariable("applicationId") UUID applicationId) {
        return applicationRespository.findOne(applicationId);
    }

    @RequestMapping(value = "users/{userId}/application", method = RequestMethod.POST)
    public Application saveUserApplication(@PathVariable("userId") UUID userId,
                                           @RequestBody Application application) {
        return applicationRespository.save(application);
    }

}
