package com.noteif.service;

import java.util.List;

public interface XmppService {

    void sendMessage(String user, String message);

    void sendMessageToMyGroup(String applicationId, String message);

    void sendMessageToUsers(List<String> users, String message);
}
