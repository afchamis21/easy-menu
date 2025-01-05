package com.chamis.andre;

import com.chamis.andre.application.ClassScanner;
import com.chamis.andre.application.Context;
import com.chamis.andre.application.MenuExecutor;
import com.chamis.andre.domain.MenuLevel;

/**
 * Entry point for starting the menu-based application.
 *
 * <p>This class provides a static method to initialize the application's context,
 * scan for {@link MenuLevel} classes, and start the menu execution loop. It serves
 * as the main entry point for consumers of the library to bootstrap and run their
 * menu-driven console applications.</p>
 *
 * <p>Usage:</p>
 * <pre>{@code
 * public class Application {
 *     public static void main(String[] args) {
 *         Menu.start(Application.class);
 *     }
 * }
 * }</pre>
 *
 * <p>The {@code start} method will:</p>
 * <ul>
 *     <li>Scan the package of the provided class for {@link MenuLevel} implementations and dependencies.</li>
 *     <li>Initialize the application context.</li>
 *     <li>Begin the interactive menu execution loop using {@link MenuExecutor}.</li>
 * </ul>
 *
 * @author Andre Chamis
 * @since 1.0.0
 */
public class Menu {
    private Menu() {
    }

    /**
     * Starts the menu-based application.
     *
     * <p>This method initializes the application by scanning the package of the provided
     * class for {@link MenuLevel} implementations and dependencies. It sets up the application
     * context, resolves dependencies, and begins the menu execution loop.</p>
     *
     * @param clazz the class whose package will be scanned for {@link MenuLevel} implementations
     *              and injectable components
     * @throws RuntimeException if initialization fails or dependencies cannot be resolved
     * @since 1.0.0
     */
    public static void start(Class<?> clazz) {
        ClassScanner.init(clazz);
        Context.getContext().postInit();

        MenuExecutor executor = new MenuExecutor();
        executor.run();
    }
}
