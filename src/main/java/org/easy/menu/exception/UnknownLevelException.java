package org.easy.menu.exception;

import org.easy.menu.domain.MenuLevel;

public class UnknownLevelException extends Exception {
    public UnknownLevelException(Class<? extends MenuLevel> level) {
        super("Level: " + level.getCanonicalName() + " is not present on the Autowired levels list");
    }
}
