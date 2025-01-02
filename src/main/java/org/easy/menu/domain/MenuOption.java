package org.easy.menu.domain;

/**
 * Represents a menu option within a hierarchical menu structure.
 * A {@code MenuOption} is an executable action that is associated with a specific {@link MenuLevel}.
 * Each option defines a label for display and the corresponding menu level it relates to.
 *
 * <p>This abstract class is intended to be extended by concrete implementations
 * that define specific behaviors for menu options.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public abstract class MenuOption implements Action {

    /**
     * Retrieves the {@link MenuLevel} associated with this menu option.
     *
     * @return the {@code MenuLevel} this option is linked to.
     * @since 1.0.0
     */
    public abstract Class<? extends MenuLevel> getLevel();
}

