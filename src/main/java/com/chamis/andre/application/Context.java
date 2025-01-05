package com.chamis.andre.application;

import com.chamis.andre.annotation.Action;
import com.chamis.andre.domain.MenuLevel;
import com.chamis.andre.exception.MissingHomeMenuException;
import com.chamis.andre.exception.MultipleHomeException;
import com.chamis.andre.exception.UnknownLevelException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the navigation context for menus, maintaining the current state and the list of available menu levels.
 *
 * <p>The {@code Context} class follows the Singleton pattern to ensure only one instance exists during the
 * application's runtime. It controls navigation between different menu levels and manages the initial "home"
 * level of the application.</p>
 *
 * <p>Menu options for navigation are automatically determined by scanning for methods in {@link MenuLevel}
 * annotated with {@link Action}.</p>
 *
 * @since 1.0.0
 */
@Getter
public class Context {
    /**
     * Singleton instance of the {@code Context}.
     * Ensures that only one instance of the context exists throughout the application.
     */
    private static Context instance = null;

    /**
     * List of all menu levels available in the application.
     */
    private final List<MenuLevel> levels = new ArrayList<>();

    /**
     * The current menu level in the application.
     */
    private MenuLevel currentLevel = null;

    /**
     * The home menu level, serving as the starting point of navigation.
     */
    private MenuLevel home = null;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private Context() {}

    /**
     * Returns the singleton instance of the context.
     *
     * <p>If the instance has not yet been created, it will be initialized. This method ensures that only one
     * {@code Context} instance exists during the application's runtime.</p>
     *
     * @return the singleton instance of the context
     * @since 1.0.0
     */
    public static Context getContext() {
        if (instance == null) {
            instance = new Context();
        }

        return instance;
    }

    /**
     * Adds a menu level to the list of available levels in the context.
     *
     * <p>This method allows adding new menu levels as they are discovered or created.
     * Actions for each menu level are dynamically determined by scanning for annotated methods
     * using {@link Action}.</p>
     *
     * @param level the menu level to add
     * @since 1.0.0
     */
    public void addLevel(MenuLevel level) {
        levels.add(level);
    }

    /**
     * Finalizes the initialization of the context by setting the current level to the "home" level.
     *
     * <p>If the "home" level has not been defined, a {@link MissingHomeMenuException} will be thrown.</p>
     *
     * @throws MissingHomeMenuException if the "home" level has not been set
     * @since 1.0.0
     */
    public void postInit() {
        if (this.home == null) {
            throw new MissingHomeMenuException();
        }

        this.currentLevel = this.home;
    }

    /**
     * Sets the "home" level of the menu, which will be the starting point of navigation.
     *
     * <p>If the "home" level has already been set, an error will be thrown.</p>
     *
     * @param home the menu level to set as "home"
     * @throws MultipleHomeException if the "home" level has already been set
     * @since 1.0.0
     */
    public void setHome(MenuLevel home) {
        if (this.home != null) {
            throw new MultipleHomeException(this.home, home);
        }

        this.home = home;
    }

    /**
     * Navigates to the specified menu level if it exists.
     *
     * <p>This method searches for the target menu level in the list of available levels and sets it as the current level.
     * If the level is not found, an {@link UnknownLevelException} will be thrown.</p>
     *
     * <p>Navigation options are automatically derived from methods annotated with {@link Action}.</p>
     *
     * @param target the class of the menu level to navigate to
     * @throws UnknownLevelException if the specified menu level is not found
     * @since 1.0.0
     */
    public void navigate(Class<? extends MenuLevel> target) throws UnknownLevelException {
        this.currentLevel = levels.stream()
                .filter(l -> l.getClass().equals(target))
                .findFirst()
                .orElseThrow(() -> new UnknownLevelException(target));
    }
}
