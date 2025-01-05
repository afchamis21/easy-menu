package org.easy.menu.annotation;

import org.easy.menu.domain.MenuLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks a method within a {@link MenuLevel} class as a menu option action.
 *
 * <p>This annotation is used to define menu options directly on methods of a {@link MenuLevel}.
 * Each annotated method will be treated as a selectable option in the menu system, with the
 * provided label displayed to the user. Methods annotated with {@code @Action} must match
 * the following signature:</p>
 *
 * <pre>{@code
 * public void methodName();
 * }</pre>
 *
 * <p>The display order of options can be controlled using the {@code priority} attribute. Lower
 * values indicate higher priority and earlier display in the menu.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

    /**
     * Specifies the label to display for this menu option.
     *
     * <p>The label is shown to the user in the menu and should be concise yet descriptive of the action.</p>
     *
     * @return the label to display for this action
     * @since 1.0.0
     */
    String label();

    /**
     * Specifies the display order of this menu option.
     *
     * <p>Options with lower order values are displayed first in the menu. If two options have
     * the same order, their display order is undefined and should be avoided by assigning unique
     * order values to each option.</p>
     *
     * @return the display order of the menu option
     * @since 1.0.0
     */
    int order();
}
