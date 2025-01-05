package com.chamis.andre.exception;

import com.chamis.andre.domain.MenuLevel;

/**
 * Exception thrown when no home menu is set for the application.
 *
 * <p>This exception is used to indicate that the application lacks a designated
 * "home" menu, which is required for proper navigation initialization. The home
 * menu can be set using the {@code @Home} annotation on a {@link MenuLevel}.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public class MissingHomeMenuException extends RuntimeException {

    /**
     * Constructs a new {@code MissingHomeMenuException} with a default error message.
     *
     * <p>The error message indicates that no menus have been marked as the home menu,
     * which is necessary for the application to function correctly.</p>
     *
     * @since 1.0.0
     */
    public MissingHomeMenuException() {
        super("No menus are set as home for the application");
    }
}

