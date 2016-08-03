package com.noteif.domain;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "xmpp_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmppUser extends AbstractEntity {

    private String username;
    private String password;
}
