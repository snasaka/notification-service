package com.noteif.service;

import java.util.List;
import java.util.UUID;

import com.noteif.domain.XmppUser;

public interface XmppService {

    void sendMessage(String user, String message);
    void createUsers(List<XmppUser> xmppUser, UUID applicationId);
    void sendMessageToMyGroup(String applicationId, String message);
    void sendMessageToUsers(List<String> users, String message);
}
