package org.easy.menu.util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);
    public static long getLongInput(String s) {
        try {
            System.out.print(s);
            long valor = scanner.nextLong();
            scanner.nextLine();
            return valor;
        } catch (Exception e) {
            System.out.println("Enter a valid long number!");
            scanner.nextLine();
            return getLongInput(s);
        }
    }
}
