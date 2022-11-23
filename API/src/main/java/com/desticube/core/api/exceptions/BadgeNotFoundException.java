package com.desticube.core.api.exceptions;

public class BadgeNotFoundException extends RuntimeException {
    public BadgeNotFoundException() {
        super("<red>Badge does not exist");
    }
}
