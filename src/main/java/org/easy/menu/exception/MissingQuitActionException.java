package org.easy.menu.exception;

/**
 * Exception thrown when no quit action is configured for the application.
 *
 * @since 1.0.0
 */
public class MissingQuitActionException extends RuntimeException {
    public MissingQuitActionException() {
        super("No Quit Actions are configured for the application");
    }
}
