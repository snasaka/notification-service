package com.noteif.service;


import com.noteif.config.XmppConfig;
import com.noteif.domain.Application;
import com.noteif.domain.Transaction;
import com.noteif.domain.XmppUser;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.noteif.repository.ApplicationRespository;
import com.noteif.repository.TransactionRepository;
import org.igniterealtime.restclient.entity.SessionEntities;
import org.igniterealtime.restclient.entity.SessionEntity;
import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.im.roster.RosterManager;

@Service
public class XmppServiceImpl implements XmppService {

    private XmppClient xmppClient;

    RestTemplate restTemplate = new RestTemplate();

    private String BASE_CLIENT_URL = "";

    private String authKey = "";

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ApplicationRespository applicationRespository;

    @Autowired
    public XmppServiceImpl(XmppConfig xmppConfig) {
        BASE_CLIENT_URL = "http://" + xmppConfig.getHost() + ":" + xmppConfig.getApiPort() + "/plugins/restapi/v1/";
        authKey = xmppConfig.getAuthKey();

        TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
                .hostname(xmppConfig.getHost())
                .port(5222)
                .secure(false)
                .build();

        try {
            xmppClient = XmppClient.create(xmppConfig.getHost(), tcpConfiguration);
            xmppClient.connect();
            xmppClient.login("admin", "admin");

            System.out.println("Session ID: " + xmppClient.getConnectedResource());
        } catch(XmppException e) {
            System.out.println("Exception Occurred ::" + e);
        }
    }

    @Override
    public void sendMessage(UUID applicationId, String username, String message) {
        List<String> userSessions = retrieveJID(applicationId + "-" + username);

        if (!CollectionUtils.isEmpty(userSessions)) {
            for (String str : userSessions) {
                Jid jid = Jid.of(str);
                xmppClient.send(new Message(jid, Message.Type.CHAT, message));
                transactionRepository.save(new Transaction(applicationId, username, jid.toString(), "", message));
            }

            /* Listeners */
            xmppClient.addOutboundMessageListener(s -> {
                Message sentMessage = s.getMessage();
                System.out.println("Message Sent : " + sentMessage);
            });

            xmppClient.addInboundMessageListener(e -> {
                Message receivedMessage = e.getMessage();
                System.out.println("Message Received : " + receivedMessage);
            });

            xmppClient.getManager(RosterManager.class).addRosterListener(e -> {
                System.out.println("roster changes : " + e);
            });
        }
    }

    @Override
    public void sendMessageToMyGroup(UUID applicationId, String message) {
        /*
            1. Find Usernames of all users of the application By applicationId.
            2. Loop over each user and get all sessions associated to this user
               and send message to all those sessions.
         */
        List<String> usernames = findUsersByApplicationID(applicationId);

        if (!CollectionUtils.isEmpty(usernames)) {
            for (String username : usernames) {
                sendMessage(applicationId, username, message);
            }
        }
    }

    @Override
    public void sendMessageToUsers(UUID applicationId, List<String> usernames, String message) {
        for (String username : usernames) {
            sendMessage(applicationId, username, message);
        }
    }

    @Override
    public void createUsers(List<XmppUser> xmppUsers, UUID applicationId) {
        xmppUsers.forEach(xmppUser -> {
            createXmppUser(applicationId, xmppUser);
        });
    }


    @Override
    public void createXmppUser(UUID applicationId, XmppUser xmppUser) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(applicationId.toString() + "-" + xmppUser.getUsername());
        userEntity.setPassword(xmppUser.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authKey);
        httpHeaders.set("Content-Type", "application/xml");
        HttpEntity<UserEntity> httpEntity = new HttpEntity<>(userEntity, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(BASE_CLIENT_URL + "users",
                    HttpMethod.POST, httpEntity, HttpStatus.class);
        } catch (HttpClientErrorException ex)   {
            if (ex.getStatusCode().value() != 409) {
                throw ex;
            }
        }
    }

    private HttpEntity<String> buildEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authKey);

        return new HttpEntity<>(httpHeaders);
    }

    private List<String> retrieveJID(String username) {

        ResponseEntity<SessionEntities> sessions = restTemplate.exchange(BASE_CLIENT_URL + "sessions/" + username,
                HttpMethod.GET, buildEntity(), SessionEntities.class);


        List<SessionEntity> sessionEntities = sessions.getBody().getSessions();

        if (CollectionUtils.isEmpty(sessionEntities)) {
            return null;
        }

        return sessionEntities.stream().map(sessionEntity -> sessionEntity.getSessionId()).collect(Collectors.toList());

    }

    private List<String> findUsersByApplicationID(UUID applicationId) {
        Application application = applicationRespository.findOne(applicationId);
        return application.getXmppUsers()
                .stream()
                .map(XmppUser::getUsername)
                .collect(Collectors.toList());
    }
}
