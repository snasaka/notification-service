package com.noteif.controller;

import com.noteif.service.XmppService;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SendMessageController {

    @Autowired
    private XmppService xmppService;

    @PostMapping(value = "api/send")
    public void sendMessage(@RequestParam("username") String username, @RequestBody String message, @RequestParam("applicationId") UUID applicationId) {
        xmppService.sendMessage(applicationId, username, message);
    }

    @PostMapping(value = "api/sendMessageToApplication")
    public void sendMessageToApplication(@RequestParam("applicationId") UUID applicationId, @RequestBody String message) {
        xmppService.sendMessageToMyGroup(applicationId, message);
    }

    @PostMapping(value= "api/sendMessageToUsers")
    public void sendMessageToGivenUsers(@RequestParam("usernames") List<String> usernames, @RequestBody String message, @RequestParam("applicationId") UUID applicationId) {
        xmppService.sendMessageToUsers(applicationId, usernames, message);
    }
}
