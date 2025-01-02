package org.easy.menu.exception;

public class MissingQuitActionException extends RuntimeException {
    public MissingQuitActionException() {
        super("No Quit Actions are configured for the application");
    }
}
