package com.noteif.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.noteif.domain.Application;
import com.noteif.domain.User;
import com.noteif.domain.UserProfile;
import com.noteif.domain.XmppUser;
import com.noteif.repository.UserProfileRepository;
import com.noteif.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @RequestMapping({ "/user", "/me" })
    public Map<String, String> user(OAuth2Authentication auth) {
        Map<String, String> map = new LinkedHashMap<>();
        User user = (User) auth.getPrincipal();
        map.put("name", user.getFirstName()+" "+user.getLastName());
        User existingUser = userRepository.findByExternalProviderId(user.getExternalProviderId());
        if(existingUser == null) {
            userRepository.save(user);
            userProfileRepository.save(new UserProfile(user, ImmutableList.of(new Application("test app", ImmutableList.of(new XmppUser("test_xmpp_user", "password"))))));
        } else {
            user = existingUser;
        }
        map.put("username", user.getUsername());
        map.put("id", user.getId().toString());
        map.put("providerId", user.getExternalProviderId());
        return map;
    }
}
