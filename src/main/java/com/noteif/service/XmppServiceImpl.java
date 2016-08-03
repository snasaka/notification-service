package com.noteif.service;

/**
 * Created by nasakas on 8/2/2016.
 */

import com.noteif.config.XmppConfig;
import java.util.List;
import org.igniterealtime.restclient.entity.SessionEntities;
import org.igniterealtime.restclient.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    public void sendMessage(String user, String message) {
        xmppClient.send(new Message(toUserJID(user), Message.Type.CHAT, message));

        /* Listeners */
        xmppClient.addOutboundMessageListener(s -> {
            Message sentMessage = s.getMessage();
            System.out.println("Message Sent : "+ sentMessage);
        });

        xmppClient.addInboundMessageListener(e -> {
            Message receivedMessage = e.getMessage();
            System.out.println("Message Received : "+ receivedMessage);
        });

        xmppClient.getManager(RosterManager.class).addRosterListener(e -> {
            System.out.println("roster changes : "+e);
        });
    }

    private Jid toUserJID(String username) {
        return Jid.of(retrieveJID(username));
    }

    private HttpEntity<String> buildEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authKey);

        return new HttpEntity<>(httpHeaders);
    }

    private String retrieveJID(String username) {

        ResponseEntity<SessionEntities> sessions = restTemplate.exchange(BASE_CLIENT_URL + "sessions/admin",
                HttpMethod.GET, buildEntity(), SessionEntities.class);


        List<SessionEntity> sessionEntities = sessions.getBody().getSessions();

        if (CollectionUtils.isEmpty(sessionEntities)) {
            return null;
        }

        return sessions.getBody().getSessions().get(0).getSessionId();
    }
}
