package org.easy.menu.exception;

import org.easy.menu.domain.MenuLevel;

public class MultipleHomeException extends RuntimeException {
    public MultipleHomeException(MenuLevel curr, MenuLevel other) {
        super("Multiple Home menus detected! " + curr.getClass().getCanonicalName() + ", " + other.getClass().getCanonicalName());
    }
}
