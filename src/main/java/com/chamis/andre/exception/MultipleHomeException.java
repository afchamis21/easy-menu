package com.chamis.andre.exception;


import com.chamis.andre.domain.MenuLevel;

/**
 * Exception thrown when multiple home menus are detected in the application.
 *
 * <p>This exception is used to indicate that more than one {@link MenuLevel} has been
 * annotated as the home menu using the {@code @Home} annotation. The application requires
 * exactly one home menu to initialize correctly, and this conflict must be resolved
 * to proceed.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public class MultipleHomeException extends RuntimeException {

    /**
     * Constructs a new {@code MultipleHomeException} with details about the conflicting home menus.
     *
     * <p>The error message includes the canonical names of the two {@link MenuLevel} classes
     * that were both marked as the home menu, helping identify the conflict.</p>
     *
     * @param curr  the current {@link MenuLevel} marked as home
     * @param other the conflicting {@link MenuLevel} also marked as home
     * @since 1.0.0
     */
    public MultipleHomeException(MenuLevel curr, MenuLevel other) {
        super("Multiple Home menus detected! " + curr.getClass().getCanonicalName() + ", " + other.getClass().getCanonicalName());
    }
}

