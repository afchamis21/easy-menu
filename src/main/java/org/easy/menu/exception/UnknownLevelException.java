package org.easy.menu.exception;

import org.easy.menu.domain.MenuLevel;

/**
 * Exception thrown when attempting to navigate to an unknown menu level.
 *
 * @since 1.0.0
 */
public class UnknownLevelException extends Exception {
    public UnknownLevelException(Class<? extends MenuLevel> level) {
        super("Level: " + level.getCanonicalName() + " is not present on the Autowired levels list");
    }
}
