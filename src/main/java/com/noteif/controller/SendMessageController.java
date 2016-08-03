package com.noteif.controller;

import com.noteif.service.XmppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {

    @Autowired
    private XmppService xmppService;

    @RequestMapping(value = "send")
    public void sendMessage() {
        xmppService.sendMessage("admin", "sent from controller");
    }
}
