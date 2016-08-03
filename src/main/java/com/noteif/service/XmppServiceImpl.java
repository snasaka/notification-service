package com.noteif.service;

/**
 * Created by nasakas on 8/2/2016.
 */

import com.noteif.config.XmppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.im.roster.RosterManager;

@Service
public class XmppServiceImpl implements XmppService {

    private XmppClient xmppClient;

    @Autowired
    public XmppServiceImpl(XmppConfig xmppConfig) {

        TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
                .hostname(xmppConfig.getHost())
                .port(5222)
                .secure(false)
                .build();

        xmppClient = XmppClient.create(xmppConfig.getHost(), tcpConfiguration);

        try {
            System.out.println("Connecting to Host " + xmppConfig.getHost() + ":" + xmppConfig.getPort());
            Jid jid =  Jid.of("admin");
            xmppClient.connect(jid);
            xmppClient.login("admin", "admin");
        } catch(XmppException e) {
            System.out.println("Exception Occurred ::" + e);
        }
    }

    public void sendMessage() {

        xmppClient.send(new Message(Jid.of("admin"), Message.Type.CHAT, "This is test"));

        // Listen for messages
        xmppClient.addInboundMessageListener(e -> {
            Message message = e.getMessage();
            System.out.println("message received : "+e);
            // Handle inbound message.
        });

        // Listen for roster pushes
        xmppClient.getManager(RosterManager.class).addRosterListener(e -> {
            // Roster has changed
            System.out.println("roster changes : "+e);
        });


    }

    @Override
    public void sendMessage(String user, String message) {
        xmppClient.send(new Message(Jid.of(user), Message.Type.CHAT, message));
    }
}
