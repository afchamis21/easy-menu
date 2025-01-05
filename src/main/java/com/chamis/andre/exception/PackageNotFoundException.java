package com.chamis.andre.exception;

/**
 * Exception thrown when a specified package is not found.
 *
 * <p>This exception is used to indicate that a required package or directory could not be located
 * during runtime. It typically occurs when scanning for classes within a specific package fails
 * due to an incorrect or missing package path.</p>
 *
 * @since 1.0.0
 */
public class PackageNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code PackageNotFoundException} with the specified detail message.
     *
     * <p>The provided message typically includes information about the missing package
     * or directory to aid in debugging the issue.</p>
     *
     * @param message the detail message describing the missing package
     * @since 1.0.0
     */
    public PackageNotFoundException(String message) {
        super(message);
    }
}

