package org.example;

import java.util.Scanner;

public class Input {
    private static final Scanner scan = new Scanner(System.in);

    public static Integer getInteger(String question) {
        try {
            View.variableMessage(question);
            return Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static Double getDouble(String question) {
        try {
            View.variableMessage(question);
            return Double.parseDouble(scan.nextLine());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static String getString(String question) {
        View.variableMessage(question);
        return scan.nextLine();
    }
}
