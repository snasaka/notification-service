package com.noteif.controller;

import com.noteif.service.XmppService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SendMessageController {

    @Autowired
    private XmppService xmppService;

    @PostMapping(value = "api/send")
    public void sendMessage(@RequestParam("username") String username, @RequestBody String message) {
        xmppService.sendMessage(username, message);
    }

    @PostMapping(value = "api/sendMessageToApplication")
    public void sendMessageToApplication(@RequestParam("applicationId") String applicationId, @RequestBody String message) {
        xmppService.sendMessageToMyGroup(applicationId, message);
    }

    @PostMapping(value= "api/sendMessageToUsers")
    public void sendMessageToGivenUsers(@RequestParam("usernames") List<String> usernames, @RequestBody String message) {
        xmppService.sendMessageToUsers(usernames, message);
    }
}
