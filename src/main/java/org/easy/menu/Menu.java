package org.easy.menu;

import org.easy.menu.application.ClassScanner;
import org.easy.menu.application.Context;
import org.easy.menu.application.MenuExecutor;

public class Menu {
    public static void start(Class<?> clazz) {
        ClassScanner.init(clazz);
        Context.getContext().postInit();

        MenuExecutor executor = new MenuExecutor();
        try {
            executor.run();
        } catch (Exception e) {
            System.out.println("Exception throw while running menu... " + e.getLocalizedMessage());
        }
    }
}
