package org.easy.menu.exception;

/**
 * Exception thrown when no home menu is set for the application.
 *
 * @since 1.0.0
 */
public class MissingHomeMenuException extends RuntimeException {
    public MissingHomeMenuException() {
        super("No menus are set as home for the application");
    }
}
