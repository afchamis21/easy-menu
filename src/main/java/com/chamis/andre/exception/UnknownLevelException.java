package com.chamis.andre.exception;

import com.chamis.andre.domain.MenuLevel;

/**
 * Exception thrown when attempting to navigate to an unknown menu level.
 *
 * <p>This exception is used to indicate that the application attempted to navigate to a
 * {@link MenuLevel} that has not been registered or is otherwise unavailable in the
 * current context.</p>
 *
 * @since 1.0.0
 */
public class UnknownLevelException extends Exception {

    /**
     * Constructs a new {@code UnknownLevelException} with the specified {@link MenuLevel} class.
     *
     * <p>The exception message includes the canonical name of the unregistered {@code MenuLevel}
     * to provide clarity on the cause of the error.</p>
     *
     * @param level the {@code MenuLevel} class that was not found in the autowired levels list
     * @since 1.0.0
     */
    public UnknownLevelException(Class<? extends MenuLevel> level) {
        super("Level: " + level.getCanonicalName() + " is not present on the autowired levels list");
    }
}

