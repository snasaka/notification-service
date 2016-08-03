package com.noteif.controller;

import java.util.UUID;

import com.noteif.domain.User;
import com.noteif.domain.UserProfile;
import com.noteif.repository.UserProfileRepository;
import com.noteif.repository.UserRepository;
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
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "users/{providerId}/profile", method = RequestMethod.GET)
    public UserProfile getUserProfile(@PathVariable("providerId") String providerId) {
        User user = userRepository.findByExternalProviderId(providerId);
        if(user != null) {
            return userProfileRepository.findByUserId(user.getId());
        }
        return null;
    }

    @RequestMapping(value = "users/{providerId}/profile", method = RequestMethod.POST)
    public UserProfile saveUserProfile(@PathVariable("providerId") String providerId, @RequestBody UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

}
