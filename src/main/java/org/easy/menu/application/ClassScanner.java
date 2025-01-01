package org.easy.menu.application;

import lombok.SneakyThrows;
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.domain.Injectable;
import org.easy.menu.domain.MenuOption;
import org.easy.menu.exception.PackageNotFoundException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * Scans packages for classes that can be instantiated and managed by the application, including
 * {@link MenuLevel} subclasses and classes annotated with {@link Injectable}.
 * This class handles the discovery, instantiation, and dependency resolution of such classes.
 *
 * <p>The {@code ClassScanner} is responsible for scanning the specified package, identifying
 * relevant classes, and managing their instantiation based on their constructor dependencies.
 * It ensures that all dependencies are available before attempting to instantiate a class,
 * and handles the initialization of menu levels in the context of the application.</p>
 *
 * <p>Classes discovered by the scanner are instantiated and managed through a {@link Context},
 * which handles the navigation between different menu levels once they are instantiated.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public class ClassScanner {
    private static final Set<Class<?>> deps = new HashSet<>();
    private static final Map<Class<?>, Object> instantiatedObjects = new HashMap<>();

    /**
     * Initializes the class scanner for a given class.
     * This method triggers the scanning of the specified class' package, finds relevant
     * {@link MenuLevel} subclasses, {@link MenuOption} subclasses and {@link Injectable} classes, and attempts to
     * instantiate them.
     *
     * <p>After instantiation, the {@link Context} is initialized, and the {@code postInit} method
     * is called to set the initial menu level.</p>
     *
     * @param clazz the class to use for scanning the package
     * @since 1.0.0
     */
    public static void init(Class<?> clazz) {
        findSubclasses(clazz);
        instantiateClasses();
        Context ctx = Context.getContext();
        ctx.postInit();
    }

    /**
     * Finds subclasses of the given class within the package and identifies classes
     * that are marked as {@link Injectable} or extend {@link MenuLevel}/{@link MenuOption}.
     *
     * <p>This method scans the directory corresponding to the package of the given class,
     * loading and identifying relevant classes.</p>
     *
     * @param clazz the class to start scanning for subclasses
     * @throws PackageNotFoundException if the package or directory cannot be found
     * @since 1.0.0
     */
    @SneakyThrows
    private static void findSubclasses(Class<?> clazz) {
        String packageName = clazz.getPackageName();

        String path = packageName.replace(".", "/");
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null) {
            throw new PackageNotFoundException("Package not found: " + packageName);
        }

        File directory = new File(resource.getFile());
        if (!directory.exists()) {
            throw new PackageNotFoundException("Directory not found: " + resource.getFile());
        }

        scanDirectory(directory, packageName);
    }

    /**
     * Scans a directory for classes and identifies those that are subclasses of
     * {@link MenuLevel}/{@link MenuOption} or annotated with {@link Injectable}.
     *
     * <p>The method recursively scans subdirectories and adds any identified classes to
     * the set of dependencies.</p>
     *
     * @param directory the directory to scan
     * @param packageName the base package name
     * @throws ClassNotFoundException if a class cannot be found
     * @since 1.0.0
     */
    private static void scanDirectory(File directory, String packageName) throws ClassNotFoundException {
        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className =  packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);

                if (MenuLevel.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                    deps.add(clazz);
                } else if (clazz.isAnnotationPresent(Injectable.class)) {
                    deps.add(clazz);
                } else if (MenuOption.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                    deps.add(clazz);
                }
            }
        }
    }

    /**
     * Instantiates all classes that have been identified and resolved for dependencies.
     *
     * <p>The method attempts to instantiate each class by checking its constructors for
     * available dependencies. Once all dependencies are available, it creates instances
     * and adds them to the context.</p>
     *
     * @throws RuntimeException if any dependencies cannot be resolved
     * @since 1.0.0
     */
    private static void instantiateClasses() {
        boolean instantiatedAtLeastOneClass;
        do {
            instantiatedAtLeastOneClass = false;
            Iterator<Class<?>> iterator = deps.iterator();

            while (iterator.hasNext()) {
                Class<?> clazz = iterator.next();
                try {
                    Constructor<?>[] constructors = clazz.getDeclaredConstructors();

                    for (Constructor<?> constructor : constructors) {
                        Class<?>[] parameterTypes = constructor.getParameterTypes();

                        if (allDependenciesAvailable(parameterTypes)) {
                            Object[] parameters = getDependencies(parameterTypes);

                            Object instance = constructor.newInstance(parameters);

                            iterator.remove();
                            removeDependenciesFromSet(parameterTypes);
                            instantiatedObjects.put(clazz, instance);

                            instantiatedAtLeastOneClass = true;

                            if (instance instanceof MenuLevel) {
                                handleMenuLevel((MenuLevel) instance);
                            } else if (instance instanceof MenuOption) {
                                handleMenuOption((MenuOption) instance);
                            }
                            break;
                        }
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    System.out.println("Error instantiating class: " + clazz.getCanonicalName() + ". " + e.getLocalizedMessage());
                }
            }
        } while (instantiatedAtLeastOneClass && !deps.isEmpty());

        if (!deps.isEmpty()) {
            System.out.println("Error instantiating the following dependencies: " + deps);
            throw new RuntimeException();
        }
    }

    /**
     * Checks if all dependencies for the given parameter types are available in the instantiated objects map.
     *
     * @param parameterTypes the parameter types of a class constructor
     * @return {@code true} if all dependencies are available, {@code false} otherwise
     * @since 1.0.0
     */
    private static boolean allDependenciesAvailable(Class<?>[] parameterTypes) {
        for (Class<?> paramType : parameterTypes) {
            if (!instantiatedObjects.containsKey(paramType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resolves the dependencies for a given constructor's parameter types.
     *
     * @param parameterTypes the parameter types of a class constructor
     * @return an array of dependencies for the constructor
     * @since 1.0.0
     */
    private static Object[] getDependencies(Class<?>[] parameterTypes){
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            parameters[i] = instantiatedObjects.get(paramType);
        }
        return parameters;
    }

    /**
     * Removes the resolved dependencies from the set of pending dependencies.
     *
     * @param parameterTypes the parameter types of a class constructor
     * @since 1.0.0
     */
    private static void removeDependenciesFromSet(Class<?>[] parameterTypes) {
        for (Class<?> paramType : parameterTypes) {
            deps.remove(paramType);
        }
    }

    /**
     * Handles a {@link MenuLevel} instance by adding it to the context.
     * If the level is designated as the "home" level, it will be set as the home in the context.
     *
     * @param level the menu level to be added
     * @since 1.0.0
     */
    private static void handleMenuLevel(MenuLevel level) {
        Context ctx = Context.getContext();
        ctx.addLevel(level);

        if (level.isHome()) {
            ctx.setHome(level);
        }
    }

    /**
     * Handles a {@link MenuOption} instance by adding it to the context.
     *
     * @param option the menu option to be added
     * @since 1.0.0
     */
    private static void handleMenuOption(MenuOption option) {
        Context ctx = Context.getContext();
        ctx.addOption(option);
    }
}
