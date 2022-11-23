package com.desticube.core.api.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException() {
        super("<red>Player is not online");
    }
}
