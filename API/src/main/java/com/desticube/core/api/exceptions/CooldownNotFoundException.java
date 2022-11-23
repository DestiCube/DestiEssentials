package com.desticube.core.api.exceptions;

public class CooldownNotFoundException extends RuntimeException {
    public CooldownNotFoundException() {
        super("<red>Cooldown does not exist");
    }
}
