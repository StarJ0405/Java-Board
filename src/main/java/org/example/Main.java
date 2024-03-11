package org.example;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    private static final LinkedHashMap<Integer, Post> posts = new LinkedHashMap<Integer, Post>();
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        main:
        while (true) {
            System.out.print("명령어 : ");
            String cmd = scan.nextLine();
            switch (cmd.toLowerCase().replace(" ", "")) {
                case "exit":
                    System.out.println("프로그램을 종료합니다.");
                    break main;
                case "add":
                    add();
                    break;
                case "list":
                    list();
                    break;
                case "update":
                    update();
                    break;
                case "delete":
                    delete();
                    break;
                case "detail":
                    detail();
                    break;
                default:
                    System.out.println("없는 명령어입니다. 다시 입력해주세요.");
                    break;
            }
        }
    }


    private static int getEmptyNumber() {
        if (!posts.isEmpty()) {
            int max = posts.keySet().stream().max(Comparator.comparingInt(Integer::intValue)).get();
            if (max != posts.size())
                for (int i = 1; i < max; i++)
                    if (!posts.containsKey(i))
                        return i;
            return max + 1;
        } else return 1;
    }

    private static void add() {
        System.out.print("게시물 제목을 입력해주세요 : ");
        String title = scan.nextLine();
        System.out.print("게시물 내용을 입력해주세요 : ");
        String desc = scan.nextLine();
        System.out.println("게시물이 등록되었습니다.");
        posts.put(getEmptyNumber(), new Post(title, desc));
    }

    private static void list() {
        System.out.println("==================");
        for (int i : posts.keySet()) {
            System.out.println("번호 : " + i);
            System.out.println("제목 : " + posts.get(i).getTitle());
            System.out.println("==================");
        }
    }

    private static void update() {
        Integer num = getNumber("수정할 게시물 번호 : ");
        if (num != null && posts.containsKey(num)) {
            System.out.print("제목 : ");
            String title = scan.nextLine();
            System.out.print("내용 : ");
            String desc = scan.nextLine();
            Post post = posts.get(num);
            post.setTitle(title);
            post.setDescription(desc);
            System.out.printf("%d번 게시물이 수정되었습니다.\n", num);
        } else
            System.out.println("없는 게시물 번호입니다.");
    }

    private static void delete() {
        Integer num = getNumber("삭제할 게시물 번호 : ");
        if (posts.containsKey(num)) {
            System.out.printf("%d번 게시물이 삭제되었습니다.\n", num);
            posts.remove(num);
        } else
            System.out.println("없는 게시물 번호입니다.");
    }

    private static void detail() {
        Integer num = getNumber("상세보기 할 게시물 번호를 입력해주세요 : ");
        if (num != null && posts.containsKey(num)) {
            System.out.println("==================");
            System.out.println("번호 : " + num);
            System.out.println("제목 : " + posts.get(num).getTitle());
            System.out.println("내용 : " + posts.get(num).getDescription());
            System.out.println("==================");
        } else
            System.out.println("없는 게시물 번호입니다.");

    }

    private static Integer getNumber(String question) {
        try {
            System.out.print(question);
            return Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}