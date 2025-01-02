package org.easy.menu.application;

import org.easy.menu.domain.Action;
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.domain.MenuOption;
import org.easy.menu.domain.QuitAction;
import org.easy.menu.util.InputUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the execution of the menu navigation system.
 *
 * <p>The {@code MenuExecutor} class manages the main application loop for navigating through menu levels,
 * displaying options, and executing actions. It interacts with the {@link Context} class to determine the
 * current menu level, retrieve available options, and manage user input.</p>
 *
 * <p>The menu loop continues running until a {@link QuitAction} is selected and executed, signaling the
 * termination of the application.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 *     MenuExecutor executor = new MenuExecutor();
 *     executor.run();
 * </pre>
 *
 * @since 1.0.0
 * @author Andre
 */
public class MenuExecutor {
    private static final long EXIT_OPTION = -1L; // Constant for exit option

    /**
     * Executes the menu loop, navigating through levels and performing actions.
     *
     * <p>This method retrieves the current menu level and its options from the {@link Context},
     * displays the menu to the user, and waits for user input to select an action. The selected
     * action is then executed. If a {@link QuitAction} is chosen, the loop terminates.</p>
     *
     * @throws Exception if any action execution encounters an error
     */
    public void run() throws Exception {
        Context ctx = Context.getContext();

        while (true) {
            MenuLevel level = ctx.getCurrentLevel();
            List<MenuOption> options = ctx.getOptions();

            Map<Long, Action> displayActions = generateDisplayActions(level, options);

            System.out.println(level.getLabel());
            Action selectedAction = selectAction(displayActions);

            if (selectedAction instanceof QuitAction) {
                selectedAction.execute();
                return;
            }

            try {
                selectedAction.execute();
            } catch (Exception e) {
                System.err.println("Error running selected action! " + e.getLocalizedMessage());
                e.printStackTrace(); // Optional: Logs stack trace for debugging
            }
        }
    }

    /**
     * Generates a map of displayable actions for the current menu level.
     *
     * <p>This method collects all actions available at the current menu level, including back actions,
     * navigation options, and custom menu options. If the level supports an exit option, it is also
     * added to the map.</p>
     *
     * @param level   the current menu level
     * @param options the menu options available at this level
     * @return a map of menu options with their corresponding identifiers
     */
    private Map<Long, Action> generateDisplayActions(MenuLevel level, List<MenuOption> options) {
        Map<Long, Action> actionMap = new HashMap<>();
        if (level.showExit()) {
            actionMap.put(EXIT_OPTION, Context.getContext().getQuitAction());
        }

        List<Action> actions = new ArrayList<>();
        level.getBackAction().ifPresent(actions::add);
        actions.addAll(level.getNavigations());
        actions.addAll(options);

        long counter = 0;
        for (Action action : actions) {
            actionMap.put(counter++, action);
        }

        return actionMap;
    }

    /**
     * Displays the menu and selects an action based on user input.
     *
     * <p>This method prompts the user to select an option from the displayed menu. It validates
     * the user input to ensure it matches one of the available options.</p>
     *
     * @param displayActions a map of actions to display
     * @return the selected action
     */
    private Action selectAction(Map<Long, Action> displayActions) {
        displayMenu(displayActions);
        while (true) {
            long option = InputUtil.getLongInput("Select an option from the menu:");

            if (displayActions.containsKey(option)) {
                return displayActions.get(option);
            }

            System.out.println("Please select a valid option!");
        }
    }

    /**
     * Displays the menu options to the user.
     *
     * <p>This method outputs each available action with its corresponding identifier.</p>
     *
     * @param displayActions a map of actions to display
     */
    private void displayMenu(Map<Long, Action> displayActions) {
        displayActions.forEach((key, action) -> {
            System.out.println(key + " - " + action.getLabel());
        });
    }
}
