package com.noteif.config;

import java.util.Map;

import com.noteif.domain.User;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;

public class CustomUserInfoTokenServices extends UserInfoTokenServices {

    public CustomUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
    }

    protected Object getPrincipal(Map<String, Object> map) {
        User user = new User();

        if (map.containsKey("id")) {
            user.setExternalProviderId((String) map.get("id"));
        }
        if (map.containsKey("username")) {
            user.setUsername((String) map.get("username"));
        }
        if( map.containsKey("email")) {
            user.setEmail((String) map.get("email"));
        }
        if( map.containsKey("first_name")) {
            user.setFirstName((String) map.get("first_name"));
        }
        if( map.containsKey("last_name")) {
            user.setLastName((String) map.get("last_name"));
        }

        return user;
    }
}
