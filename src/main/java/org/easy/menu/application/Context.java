package org.easy.menu.application;

import lombok.AccessLevel;
import lombok.Getter;
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.domain.MenuOption;
import org.easy.menu.domain.QuitAction;
import org.easy.menu.exception.*;

import java.util.*;

/**
 * Manages the navigation context for menus, maintaining the current state and the list of available menu levels.
 *
 * <p>The {@code Context} class follows the Singleton pattern to ensure only one instance exists during the
 * application's runtime. It controls navigation between different menu levels and manages the initial "home"
 * level of the application.</p>
 *
 * <p>After initialization, the context must have a defined "home" level. Otherwise, an exception will be thrown.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
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
     * Maps menu levels to their corresponding menu options.
     */
    @Getter(value = AccessLevel.NONE)
    private final Map<Class<? extends MenuLevel>, List<MenuOption>> groupedOptions = new HashMap<>();

    /**
     * The current menu level in the application.
     */
    private MenuLevel currentLevel = null;

    /**
     * The home menu level, serving as the starting point of navigation.
     */
    private MenuLevel home = null;

    /**
     * The action executed when quitting the application.
     */
    private QuitAction quitAction = null;

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
     * <p>This method allows adding new menu levels as they are discovered or created.</p>
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

        if (this.quitAction == null) {
            throw new MissingQuitActionException();
        }
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
     * @param target the class of the menu level to navigate to
     * @throws UnknownLevelException if the specified menu level is not found
     * @since 1.0.0
     */
    public void navigate(Class<? extends MenuLevel> target) throws UnknownLevelException {
        this.currentLevel = levels.stream().filter(l -> l.getClass().equals(target)).findFirst().orElseThrow(() -> new UnknownLevelException(target));
    }

    /**
     * Adds a menu option to the context, associating it with a specific menu level.
     *
     * @param option the menu option to add
     * @throws NullPointerException if the option is null
     * @since 1.0.0
     */
    public void addOption(MenuOption option) {
        Objects.requireNonNull(option);
        if (!groupedOptions.containsKey(option.getLevel())) {
            groupedOptions.put(option.getLevel(), new ArrayList<>());
        }

        groupedOptions.get(option.getLevel()).add(option);
    }

    /**
     * Retrieves the list of menu options associated with a specific menu level.
     *
     * @param level the class of the menu level
     * @return the list of menu options for the specified level
     * @since 1.0.0
     */
    public List<MenuOption> getOptions(Class<? extends MenuLevel> level) {
        return groupedOptions.getOrDefault(level, new ArrayList<>());
    }

    /**
     * Retrieves the list of menu options associated with the current menu level.
     *
     * @return the list of menu options for the current level
     * @since 1.0.0
     */
    public List<MenuOption> getOptions() {
        return getOptions(currentLevel.getClass());
    }

    /**
     * Sets the quit action for the application.
     *
     * <p>If a quit action has already been set, an exception will be thrown.</p>
     *
     * @param quitAction the quit action to set
     * @throws MultipleQuitException if a quit action has already been set
     * @since 1.0.0
     */
    public void setQuitAction(QuitAction quitAction) {
        if (this.quitAction != null) {
            throw new MultipleQuitException(this.quitAction, quitAction);
        }

        this.quitAction = quitAction;
    }
}
