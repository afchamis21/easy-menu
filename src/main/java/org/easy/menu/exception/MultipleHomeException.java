package org.easy.menu.exception;

import org.easy.menu.domain.MenuLevel;

/**
 * Exception thrown when multiple home menus are detected in the application.
 *
 * @since 1.0.0
 */
public class MultipleHomeException extends RuntimeException {
    public MultipleHomeException(MenuLevel curr, MenuLevel other) {
        super("Multiple Home menus detected! " + curr.getClass().getCanonicalName() + ", " + other.getClass().getCanonicalName());
    }
}
