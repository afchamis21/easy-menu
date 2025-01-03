package org.easy.menu.domain;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Abstract representation of a menu level in a hierarchical menu structure.
 * Each menu level is expected to define its own label, parent level, and navigation actions.
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public abstract class MenuLevel {
    /**
     * Retrieves the label for this menu level.
     *
     * @return a {@code String} representing the display name of the menu level.
     * @since 1.0.0
     */
    public abstract String getLabel();

    public Method[] getActions() {
        return Arrays.stream(this.getClass().getMethods()).filter((method -> method.isAnnotationPresent(Action.class))).toArray(Method[]::new);
    }
}

