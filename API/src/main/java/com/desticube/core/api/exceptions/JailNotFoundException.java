package com.desticube.core.api.exceptions;

public class JailNotFoundException extends RuntimeException {
    public JailNotFoundException() {
        super("<red>Jail does not exist");
    }
}
