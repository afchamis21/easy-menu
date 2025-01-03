package org.easy.menu.util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);
    public static int getIntInput(String s) {
        try {
            System.out.print(s);
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (Exception e) {
            System.out.println("Enter a valid Integer!");
            scanner.nextLine();
            return getIntInput(s);
        }
    }
}
