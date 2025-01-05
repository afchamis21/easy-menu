package com.chamis.andre.util;

import java.util.Scanner;

/**
 * Utility class for handling user input from the console.
 *
 * <p>This class provides static methods for reading input values, ensuring proper validation and error handling.
 * It supports operations like reading integer inputs with retry mechanisms for invalid entries.</p>
 *
 * @since 1.0.0
 */
public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtil() {
    }

    /**
     * Prompts the user for an integer input and handles invalid input gracefully.
     *
     * <p>This method continuously prompts the user until a valid integer is entered.
     * If the input is not a valid integer, an error message is displayed, and the prompt is repeated.</p>
     *
     * @param prompt the message to display when prompting for input
     * @return the valid integer entered by the user
     * @since 1.0.0
     */
    public static int getIntInput(String prompt) {
        try {
            System.out.print(prompt);
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (Exception e) {
            System.out.println("Enter a valid Integer!");
            scanner.nextLine();
            return getIntInput(prompt);
        }
    }
}
