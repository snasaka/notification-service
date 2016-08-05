package com.noteif.service;

import java.util.List;
import java.util.UUID;

import com.noteif.domain.XmppUser;

public interface XmppService {

    void sendMessage(UUID applicationId, String user, String message);
    void createUsers(List<XmppUser> xmppUser, UUID applicationId);
    void sendMessageToMyGroup(UUID applicationId, String message);
    void sendMessageToUsers(UUID applicationId, List<String> users, String message);

    void createXmppUser(UUID applicationId, XmppUser xmppUser);
}
