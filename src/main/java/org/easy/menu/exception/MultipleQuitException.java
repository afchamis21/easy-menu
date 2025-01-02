package org.easy.menu.exception;

import org.easy.menu.domain.QuitAction;

public class MultipleQuitException extends RuntimeException {
    public MultipleQuitException(QuitAction curr, QuitAction other) {
        super("Multiple Quit actions detected! " + curr.getClass().getCanonicalName() + ", " + other.getClass().getCanonicalName());
    }
}
