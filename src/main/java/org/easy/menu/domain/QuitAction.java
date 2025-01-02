package org.easy.menu.domain;

public abstract class QuitAction implements Action {

    @Override
    public void execute() {
        System.exit(0);
    }
}
