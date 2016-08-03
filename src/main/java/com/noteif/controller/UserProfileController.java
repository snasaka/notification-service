package com.noteif.controller;

import java.util.UUID;

import com.noteif.domain.UserProfile;
import com.noteif.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

    @Autowired
    UserProfileRepository userProfileRepository;

    @RequestMapping(value = "users/{id}/profile", method = RequestMethod.GET)
    public UserProfile getUserProfile(@PathVariable("id") UUID id) {
        return userProfileRepository.findByUserId(id);
    }

    @RequestMapping(value = "users/{id}/profile", method = RequestMethod.POST)
    public UserProfile saveUserProfile(@PathVariable("id") UUID id, @RequestBody UserProfile userProfile) {
        return userProfileRepository.findByUserId(id);
    }

}
