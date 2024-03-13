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
    public static void sendConsoleErrorMessage(){
        View.sendMessage("없는 명령어입니다. 다시 입력해주세요.");
        View.sendMessage("도움이 필요하면 help를 입력하세요.");
    }
}
