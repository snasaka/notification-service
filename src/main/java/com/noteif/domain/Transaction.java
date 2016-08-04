package com.noteif.domain;

import javax.persistence.Entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends AbstractEntity {

    private UUID applicationId;
    private UUID xmppUserId;
    private String jid;
    private String messgeSubject;
    private String messageBody;

}
