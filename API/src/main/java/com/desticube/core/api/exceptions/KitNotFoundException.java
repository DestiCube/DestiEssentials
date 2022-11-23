package com.desticube.core.api.exceptions;

public class KitNotFoundException extends RuntimeException {
    public KitNotFoundException() {
        super("<red>Kit does not exist");
    }
}
