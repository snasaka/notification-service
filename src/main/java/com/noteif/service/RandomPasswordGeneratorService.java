package com.noteif.service;

import java.util.UUID;

public class RandomPasswordGeneratorService {
    public static String generatePassword() {
        return UUID.randomUUID().toString();
    }
}
