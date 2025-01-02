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

public class MenuExecutor {
    public void run() throws Exception {
        Context ctx = Context.getContext();

        while (true) {
            MenuLevel level = ctx.getCurrentLevel();
            List<MenuOption> options = ctx.getOptions(level.getClass());

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
                System.out.println("Error running selected action! " + e.getLocalizedMessage());
            }
        }
    }

    private Map<Long, Action> generateDisplayActions(MenuLevel level, List<MenuOption> options) {
        Map<Long, Action> res = new HashMap<>();
        if (level.showExit()) {
            res.put(-1L, Context.getContext().getQuitAction());
        }

        List<Action> actions = new ArrayList<>();
        level.getBackAction().ifPresent((actions::add));
        actions.addAll(level.getNavigations());
        actions.addAll(options);

        long counter = 0;
        for (Action action : actions) {
            res.put(counter, action);
            counter++;
        }

        return res;
    }

    private Action selectAction(Map<Long, Action> displayActions) {
        displayMenu(displayActions);
        while (true) {
            long opt = InputUtil.getLongInput("Select an option from the menu:");

            if (displayActions.containsKey(opt)) {
                return displayActions.get(opt);
            }

            System.out.println("Select a valid option!");
        }
    }

    private void displayMenu(Map<Long, Action> displayActions) {
        displayActions.forEach((i, action) -> {
            System.out.println(i + " - " + action.getLabel());
        });
    }
}
