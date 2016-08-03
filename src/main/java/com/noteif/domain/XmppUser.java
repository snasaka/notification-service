package com.noteif.domain;

import javax.persistence.Entity;

import com.noteif.service.RandomPasswordGeneratorService;
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

    public XmppUser(String username) {
        this.username = username;
        this.password = RandomPasswordGeneratorService.generatePassword();
    }
}
