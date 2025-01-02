package org.easy.menu.domain;

/**
 * Represents a general action that can be executed.
 * This interface is designed to be implemented by classes
 * that define specific executable behaviors.
 *
 * <p>The {@code execute} method should encapsulate the logic
 * of the action, and it may throw exceptions to indicate
 * any issues during execution.</p>
 *
 * @since 1.0.0
 * @author Andre Chamis
 */
public interface Action {
    /**
     * Executes the action.
     *
     * <p>Implementations should provide the specific logic for the action.
     * Exceptions may be thrown if the execution encounters any issues.</p>
     *
     * @throws Exception if any error occurs during the execution of the action
     * @since 1.0.0
     */
    void execute() throws Exception;

    /**
     * Retrieves the display label for this action.
     *
     * @return a {@code String} representing the label of this action.
     * @since 1.0.0
     */
    String getLabel();
}
