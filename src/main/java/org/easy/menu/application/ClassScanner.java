package org.easy.menu.application;

import lombok.SneakyThrows;
import org.easy.menu.annotation.Home;
import org.easy.menu.annotation.Injectable;
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.exception.PackageNotFoundException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * Scans packages for classes that can be instantiated and managed by the application, including
 * {@link MenuLevel} subclasses and classes annotated with {@link Injectable}.
 *
 * <p>This class discovers and resolves dependencies, ensuring the initialization of {@link MenuLevel}
 * instances and injectable components. It also validates actions annotated with {@link org.easy.menu.annotation.Action}
 * to ensure compliance with application rules.</p>
 *
 * @author Andre Chamis
 * @since 1.0.0
 */
public class ClassScanner {
    private static final Set<Class<?>> deps = new HashSet<>();
    private static final Map<Class<?>, Object> instantiatedObjects = new HashMap<>();

    private ClassScanner() {
    }

    /**
     * Initializes the class scanner for a given class.
     *
     * <p>This method scans the package of the provided class for {@link MenuLevel} and
     * {@link Injectable} classes. After resolving all dependencies, it initializes the
     * application context by calling {@link Context#postInit()}.</p>
     *
     * @param clazz the class whose package will be scanned
     * @since 1.0.0
     */
    public static void init(Class<?> clazz) {
        findSubclasses(clazz);
        instantiateClasses();
    }

    /**
     * Scans the package of the given class for subclasses of {@link MenuLevel} or classes annotated
     * with {@link Injectable}.
     *
     * <p>This method locates all relevant classes in the package and sub-packages, ensuring they
     * can be initialized as part of the application's lifecycle.</p>
     *
     * @param clazz the class whose package will be scanned
     * @throws PackageNotFoundException if the package or directory is not found
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
     * Recursively scans the specified directory for classes that are either subclasses of
     * {@link MenuLevel} or annotated with {@link Injectable}.
     *
     * @param directory   the directory to scan
     * @param packageName the package name corresponding to the directory
     * @throws ClassNotFoundException if a class cannot be loaded
     * @since 1.0.0
     */
    private static void scanDirectory(File directory, String packageName) throws ClassNotFoundException {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);

                if (MenuLevel.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                    deps.add(clazz);
                } else if (clazz.isAnnotationPresent(Injectable.class)) {
                    deps.add(clazz);
                }
            }
        }
    }

    /**
     * Instantiates all identified classes, resolving their dependencies using constructor injection.
     *
     * <p>This method ensures that all classes, including {@link MenuLevel} and injectable components,
     * are properly instantiated and registered with the {@link Context}. It validates dependencies and
     * reports unresolved cases as errors.</p>
     *
     * @throws RuntimeException if dependencies cannot be resolved
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
     * Checks if all dependencies required by a constructor are already instantiated.
     *
     * @param parameterTypes the constructor parameter types
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
     * Retrieves the instantiated dependencies for the given constructor parameter types.
     *
     * @param parameterTypes the constructor parameter types
     * @return an array of instantiated dependencies
     * @since 1.0.0
     */
    private static Object[] getDependencies(Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = instantiatedObjects.get(parameterTypes[i]);
        }
        return parameters;
    }

    /**
     * Removes the specified dependencies from the set of pending dependencies.
     *
     * @param parameterTypes the constructor parameter types
     * @since 1.0.0
     */
    private static void removeDependenciesFromSet(Class<?>[] parameterTypes) {
        for (Class<?> paramType : parameterTypes) {
            deps.remove(paramType);
        }
    }

    /**
     * Adds a {@link MenuLevel} instance to the context and sets it as the home level if annotated with {@link Home}.
     *
     * <p>Validates all methods annotated with {@link org.easy.menu.annotation.Action} to ensure they do not
     * accept parameters, enforcing application requirements.</p>
     *
     * @param level the menu level instance
     * @since 1.0.0
     */
    private static void handleMenuLevel(MenuLevel level) {
        Method[] actions = level.getActions();
        for (Method action : actions) {
            if (action.getParameterCount() > 0) {
                throw new IllegalArgumentException("The method: " + action.getName() + " has arguments. Methods annotated with @Action cannot have any arguments!");
            }
        }

        Context ctx = Context.getContext();
        ctx.addLevel(level);

        if (level.getClass().isAnnotationPresent(Home.class)) {
            ctx.setHome(level);
        }
    }
}
