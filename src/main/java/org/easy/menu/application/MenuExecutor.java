package org.easy.menu.application;

import org.easy.menu.domain.Action;
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.util.InputUtil;
import org.easy.menu.util.StringUtil;

import java.lang.reflect.Method;
import java.util.*;

public class MenuExecutor {
    /**
     * Executes the menu loop, navigating through levels and performing actions.
     *
     * <p>This method retrieves the current menu level and its options from the {@link Context},
     * displays the menu to the user, and waits for user input to select an action. The selected
     * action is then executed.</p>
     *
     * @throws Exception if any action execution encounters an error
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

            System.out.println("-----------");
        }
    }

    private List<DisplayAction> generateDisplayActions(MenuLevel level) {
        Method[] actions = Arrays.stream(level.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Action.class))
                .sorted(Comparator.comparing(action -> action.getAnnotation(Action.class).priority()))
                .toArray(Method[]::new);

        List<DisplayAction> displayActions = new ArrayList<>();
        for (int i = 0; i < actions.length; i++) {
            Method method = actions[i];
            Action action = method.getAnnotation(Action.class);

            displayActions.add(new DisplayAction(i, action.label(), method));
        }

        return displayActions;
    }

    private DisplayAction selectAction() {
        Context ctx = Context.getContext();
        MenuLevel level = ctx.getCurrentLevel();
        List<DisplayAction> displayActions = generateDisplayActions(level);
        final Map<Integer, DisplayAction> displayActionMap = new HashMap<>();
        displayActions.forEach(displayAction -> displayActionMap.put(displayAction.value, displayAction));

        System.out.println(level.getLabel());
        displayMenu(displayActionMap);
        while (true) {
            int option = InputUtil.getIntInput("Select an option from the menu:");

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
    private void displayMenu(Map<Integer, DisplayAction> displayActions) {
        displayActions.forEach((key, action) -> System.out.println(StringUtil.padRight(String.valueOf(key), 4) + " - " + action.label));
    }

    private static class DisplayAction {
        private final String label;
        private final Method action;
        private final int value;

        private DisplayAction(int value, String label, Method action) {
            this.label = label;
            this.action = action;
            this.value = value;
        }
    }
}
