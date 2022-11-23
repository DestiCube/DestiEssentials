package com.desticube.core.api.exceptions;

public class WarpNotFoundException extends RuntimeException {
    public WarpNotFoundException() {
        super("<red>Warp does not exist");
    }
}
