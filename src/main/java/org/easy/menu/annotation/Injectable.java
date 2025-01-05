package org.easy.menu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class is injectable and can be managed by the dependency injection system.
 *
 * <p>This annotation is used to mark classes that should be considered for dependency injection.
 * When applied to a class, it signals that instances of the class can be created and injected into
 * other classes or components where needed.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {
}

