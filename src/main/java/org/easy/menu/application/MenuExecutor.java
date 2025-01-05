package org.easy.menu.application;

import org.easy.menu.annotation.Action;
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.util.InputUtil;
import org.easy.menu.util.StringUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles the execution of the menu system, including navigation and action invocation.
 *
 * <p>The {@code MenuExecutor} retrieves the current menu level from the {@link Context},
 * generates a list of menu options based on annotated methods, displays the menu,
 * and processes user input to invoke the selected action.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public class MenuExecutor {
    /**
     * Default constructor for {@code MenuExecutor}.
     *
     * <p>Initializes a new instance of the {@code MenuExecutor} class. While this class
     * primarily operates through the {@link #run()} method, this constructor allows
     * instantiation if needed for further customization or setup.</p>
     *
     * @since 1.0.0
     */
    public MenuExecutor() {
    }

    /**
     * Executes the menu loop, navigating through levels and performing actions.
     *
     * <p>This method retrieves the current menu level and its options from the {@link Context},
     * displays the menu to the user, and waits for user input to select an action. The selected
     * action is then executed.</p>
     */
    public void run() {
        Context ctx = Context.getContext();

        while (true) {
            DisplayAction selectedAction = selectAction();

            try {
                selectedAction.action.invoke(ctx.getCurrentLevel());
            } catch (Exception e) {
                System.err.println("Error running selected action! " + e.getLocalizedMessage());
                e.printStackTrace(); // Optional: Logs stack trace for debugging
            }
        }
    }

    /**
     * Generates a list of display actions based on annotated methods in the provided menu level.
     *
     * <p>This method inspects the methods of the {@link MenuLevel} class, filters those annotated with
     * {@link Action}, and creates {@link DisplayAction} objects sorted by the order defined in the annotations.</p>
     *
     * @param level the current menu level
     * @return a list of display actions
     */
    protected List<DisplayAction> generateDisplayActions(MenuLevel level) {
        Method[] actions = Arrays.stream(level.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Action.class))
                .sorted(Comparator.comparing(action -> action.getAnnotation(Action.class).order()))
                .toArray(Method[]::new);

        List<DisplayAction> displayActions = new ArrayList<>();
        for (int i = 0; i < actions.length; i++) {
            Method method = actions[i];
            Action action = method.getAnnotation(Action.class);

            displayActions.add(new DisplayAction(i, action.label(), method));
        }

        return displayActions;
    }

    /**
     * Prompts the user to select an action from the current menu level.
     *
     * <p>This method displays the current menu options, waits for valid input, and returns the
     * corresponding {@link DisplayAction} based on the user's selection.</p>
     *
     * @return the selected {@link DisplayAction}
     */
    protected DisplayAction selectAction() {
        Context ctx = Context.getContext();
        MenuLevel level = ctx.getCurrentLevel();
        List<DisplayAction> displayActions = generateDisplayActions(level);
        final Map<Integer, DisplayAction> displayActionMap = new HashMap<>();
        displayActions.forEach(displayAction -> displayActionMap.put(displayAction.value, displayAction));

        displayLabel(level);
        displayMenu(displayActionMap);
        while (true) {
            int option = InputUtil.getIntInput("Select an option from the menu: ");

            if (displayActionMap.containsKey(option)) {
                return displayActions.get(option);
            }

            System.out.println("\nPlease select a valid option!");
        }
    }

    /**
     * Displays the menu options to the user.
     *
     * <p>This method outputs each available action with its corresponding identifier.</p>
     *
     * @param displayActions a map of actions to display
     */
    protected void displayMenu(Map<Integer, DisplayAction> displayActions) {
        displayActions.forEach((key, action) -> System.out.println(StringUtil.padRight(String.valueOf(key), 2) + " - " + action.label));
    }

    /**
     * Displays the label of the current menu level.
     *
     * <p>This method creates a decorative frame around the label and prints it to the console.
     * If the label is empty, the method does nothing.</p>
     *
     * @param level the current menu level
     */
    protected void displayLabel(MenuLevel level) {
        String label = level.getLabel();
        if (label.isEmpty()) {
            return;
        }

        StringBuilder upperBorder = new StringBuilder("╔══");
        StringBuilder lowerBorder = new StringBuilder("╚══");

        for (int i = 0; i < label.length(); i++) {
            upperBorder.append("═");
            lowerBorder.append("═");
        }

        upperBorder.append("══╗");
        lowerBorder.append("══╝");

        System.out.println();
        System.out.println(upperBorder);
        System.out.println("║  " + label + "  ║");
        System.out.println(lowerBorder);
    }

    /**
     * Represents an action that can be displayed and executed from the menu.
     */
    protected static class DisplayAction {
        private final String label;
        private final Method action;
        private final int value;

        /**
         * Constructs a {@code DisplayAction} instance.
         *
         * <p>Each instance represents a menu option with its display label, associated method, and menu index.</p>
         *
         * @param value the index of the action in the menu
         * @param label the label to display for the action
         * @param action the method to invoke when the action is selected
         */
        private DisplayAction(int value, String label, Method action) {
            this.label = label;
            this.action = action;
            this.value = value;
        }
    }
}
