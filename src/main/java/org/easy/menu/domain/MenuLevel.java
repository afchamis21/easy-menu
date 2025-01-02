package org.easy.menu.domain;

import lombok.Getter;
import org.easy.menu.application.Context;
import org.easy.menu.exception.UnknownLevelException;

import java.util.List;
import java.util.Optional;

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

    public abstract Optional<MoveAction> getBackAction();

    /**
     * Retrieves the list of navigation actions available for this menu level.
     *
     * @return a {@code List} of {@link MoveAction} representing navigation options from this menu level.
     * @since 1.0.0
     */
    public abstract List<MoveAction> getNavigations();

    /**
     * Indicates whether this menu level should display an exit option.
     *
     * @return {@code true} if the exit option should be displayed, {@code false} otherwise.
     * @since 1.0.0
     */
    public boolean showExit() {
        return false;
    }

    /**
     * Indicates whether this menu level is the home level of the menu structure.
     *
     * @return {@code true} if this level is the home level, {@code false} otherwise.
     * @since 1.0.0
     */
    public boolean isHome() {
        return false;
    }

    /**
     * Represents a navigation action from one menu level to another.
     * A {@code MoveAction} allows navigating to a specific target menu level with a defined label.
     *
     * @since 1.0.0
     */
    public static class MoveAction implements Action {
        private final Class<? extends MenuLevel> target;
        private final String label;

        /**
         * Constructs a {@code MoveAction} to navigate to a specific menu level.
         *
         * @param target the {@code Class} of the target menu level.
         * @param label  a {@code String} representing the action label.
         * @since 1.0.0
         */
        public MoveAction(Class<? extends MenuLevel> target, String label) {
            this.target = target;
            this.label = label;
        }

        public void execute() throws UnknownLevelException {
            Context context = Context.getContext();
            context.navigate(target);
        }

        @Override
        public String getLabel() {
            return label;
        }
    }
}

