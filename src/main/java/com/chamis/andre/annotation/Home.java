package com.chamis.andre.annotation;

import com.chamis.andre.domain.MenuLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks a {@link MenuLevel} as the "home" level in the menu hierarchy.
 *
 * <p>This annotation is used to indicate the starting point of the menu navigation.
 * Only one {@code MenuLevel} in the application should be annotated with {@code @Home}.
 * If multiple levels are annotated, an error may occur during initialization.</p>
 *
 * <p>The {@code Context} class uses this annotation to automatically identify and set
 * the "home" level during the application setup.</p>
 *
 * @since 1.0.0
 * @see MenuLevel
 * @author Andre Chamis
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Home {
}


