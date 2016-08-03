package com.noteif.config;

import java.util.List;
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
            user.setExternalProviderId(extractId(map, "id"));
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

        if( map.containsKey("name")) {
            if(map.get("name") instanceof String) {
                String[] fullName = ((String) map.get("name")).split(" ");
                user.setFirstName(fullName[0]);
                user.setLastName(fullName[1]);
            }
        }

        if( map.containsKey("displayName")) {
            String[] fullName = ((String) map.get("displayName")).split(" ");
            user.setFirstName(fullName[0]);
            user.setLastName(fullName[1]);
        }

        if( map.containsKey("emails")) {
            List<Map<String, String>> emails = (List<Map<String,String>>) map.get("emails");
            user.setEmail(emails.get(0).get("value"));
        }

        return user;
    }

    // Hack for github auth
    private String extractId(Map<String, Object> map, String id) {
        return map.get(id) instanceof Integer ? String.valueOf((Integer) map.get(id)) : (String) map.get(id);
    }
}
