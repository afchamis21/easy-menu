package org.easy.menu.exception;

import org.easy.menu.domain.QuitAction;

/**
 * Exception thrown when multiple quit actions are detected in the application.
 *
 * @since 1.0.0
 */
public class MultipleQuitException extends RuntimeException {
    public MultipleQuitException(QuitAction curr, QuitAction other) {
        super("Multiple Quit actions detected! " + curr.getClass().getCanonicalName() + ", " + other.getClass().getCanonicalName());
    }
}
