package org.easy.menu.exception;

/**
 * Exception thrown when a specified package is not found.
 *
 * @since 1.0.0
 */
public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String message) {
        super(message);
    }
}
