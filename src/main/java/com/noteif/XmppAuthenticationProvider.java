package com.noteif;

/**
 * Created by nasakas on 8/2/2016.
 */

import config.XmppConfig;
import java.net.InetSocketAddress;
import java.net.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.extensions.httpbind.BoshConnectionConfiguration;

@Component
public class XmppAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private XmppConfig xmppConfig;

    public void connectToServer() {

        TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
                .hostname(xmppConfig.getHost())
                .port(5222)
                .build();


        BoshConnectionConfiguration boshConfiguration = BoshConnectionConfiguration.builder()
                .hostname(xmppConfig.getHost())
                .port(xmppConfig.getPort())
                .path(xmppConfig.getHttpBind())
                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("hostname", 3128)))
                .wait(60)
                .build();

        //XmppClient xmppClient = XmppClient.create(xmppConfig.getHost(), tcpConfiguration, boshConfiguration);
        XmppClient xmppClient = XmppClient.create(xmppConfig.getHost(), tcpConfiguration, boshConfiguration);
        try {
            System.out.println("Connecting to Host " + xmppConfig.getHost() + ":" + xmppConfig.getPort());
            Jid jid =  Jid.of("admin");
            //Jid jid =  Jid.of("admin", "admin", "general" );
            //xmppClient.connect(jid);
            xmppClient.connect(jid);
            //xmppClient.login("admin", "admin", "resource");
        } catch(XmppException e) {
            System.out.println("Exception Occurred ::" + e);
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //Authentication and BOSH pre-binding

        BoshConnectionConfiguration boshConfiguration = BoshConnectionConfiguration.builder()
                .hostname(xmppConfig.getHost())
                .port(xmppConfig.getPort())
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("hostname", 3128)))
                .path(xmppConfig.getHttpBind())
                //.wait(60)
                .build();

        XmppClient xmppClient = XmppClient.create(xmppConfig.getHost(), boshConfiguration);

        try {
                xmppClient.connect();
           /*
            xmppClient.connect(new Jid((String) authentication.getPrincipal()));
            xmppClient.login((String) authentication.getPrincipal(), (String) authentication.getCredentials());

            rocks.xmpp.extensions.httpbind.BoshConnection boshConnection =
                    (rocks.xmpp.extensions.httpbind.BoshConnection) xmppClient.getActiveConnection();

            String sessionId = boshConnection.getSessionId();

            // Detaches the BOSH session, without terminating it.
            long rid = boshConnection.detach();
//            System.out.println("JID: " + xmppClient.getConnectedResource());
//            System.out.println("SID: " + sessionId);
//            System.out.println("RID: " + rid);

            XmppUser xmppUser = new XmppUser();
            xmppUser.setUsername((String) authentication.getPrincipal());
            xmppUser.setJid(xmppClient.getConnectedResource().toString());
            xmppUser.setSid(sessionId);
            xmppUser.setRid(rid);

            Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

            return new UsernamePasswordAuthenticationToken(xmppUser, authentication.getCredentials(), authorities);
           */
            return new UsernamePasswordAuthenticationToken(null, null);
        } catch (XmppException e) {
            e.printStackTrace();
            return null;
            //throw new XmppAuthenticationException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
