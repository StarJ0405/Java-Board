package org.example;

import java.time.format.DateTimeFormatter;

public class View {
    public static void variableMessage(String msg) {
        System.out.print(msg);
    }

    public static void sendMessage(String msg) {
        System.out.println(msg);
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss");
    }

}
