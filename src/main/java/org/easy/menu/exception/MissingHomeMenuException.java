package org.easy.menu.exception;

public class MissingHomeMenuException extends RuntimeException {
    public MissingHomeMenuException() {
        super("No menus are set as home for the application");
    }
}
