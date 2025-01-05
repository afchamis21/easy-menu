package com.chamis.andre.domain;

import com.chamis.andre.annotation.Action;
import com.chamis.andre.application.Context;
import com.chamis.andre.exception.UnknownLevelException;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Abstract representation of a menu level in a hierarchical menu structure.
 *
 * <p>Each menu level represents a specific context or screen in the menu system.
 * Implementing classes are responsible for defining:
 * <ul>
 *     <li>The label that describes the menu level.</li>
 *     <li>Actions available at the level, which are identified via the {@link Action} annotation.</li>
 * </ul>
 *
 * <p>This abstraction provides flexibility for creating complex, nested menu structures,
 * while allowing each level to encapsulate its own behavior and options.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public abstract class MenuLevel {

    /**
     * Retrieves the label for this menu level.
     *
     * <p>The label is displayed as the title or header when this menu level is active.
     * It should provide a clear and concise description of the current level's purpose or context.</p>
     *
     * @return a {@code String} representing the display name of the menu level.
     * @since 1.0.0
     */
    public abstract String getLabel();

    /**
     * Retrieves the actions defined for this menu level.
     *
     * <p>Actions are identified by scanning the class for methods annotated with {@link Action}.
     * These actions represent the available options the user can select when this level is active.</p>
     *
     * <p>The resulting array includes all annotated methods, ordered as they are discovered
     * by reflection. The {@link Action#order()} attribute can be used to control the display
     * order of the actions.</p>
     *
     * @return an array of {@link Method} objects representing the actions available at this menu level.
     * @since 1.0.0
     */
    public Method[] getActions() {
        return Arrays.stream(this.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Action.class))
                .toArray(Method[]::new);
    }

    /**
     * Navigates to the specified target {@link MenuLevel}.
     *
     * <p>This method allows a menu level to transition to another specified menu level.
     * It delegates the navigation logic to the {@link Context} class, which handles the transition
     * between menu levels within the application's context.</p>
     *
     * @param target the target class of the {@link MenuLevel} to navigate to
     * @throws UnknownLevelException if the specified target level is not registered in the context
     * @since 1.0.0
     */
    protected void navigate(Class<? extends MenuLevel> target) throws UnknownLevelException {
        Context.getContext().navigate(target);
    }
}
