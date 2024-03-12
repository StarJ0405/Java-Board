package org.example;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final LinkedHashMap<Integer, Post> posts = new LinkedHashMap<Integer, Post>();
    private static final HashMap<String, Member> members = new HashMap<String, Member>();
    private static final Scanner scan = new Scanner(System.in);
    private static Member who = null;

    public static void main(String[] args) {
        initial();
        main:
        while (true) {
            System.out.print("명령어를 입력해주세요" + (who != null ? "[" + who.getId() + "(" + who.getNickname() + ")]" : "") + " : ");
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
                case "search":
                    search();
                    break;
                case "signup":
                    signup();
                    break;
                case "login":
                    login();
                    break;
                case "logout":
                    who = null;
                    System.out.println("로그아웃에 성공했습니다.");
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

    private static void initial() {
        posts.put(getEmptyNumber(), new Post(1, "안녕하세요 반갑습니다. 자바 공부중이에요.", "즐거운 자바시간"));
        posts.put(getEmptyNumber(), new Post(2, "자바 질문좀 할게요~", "제곧내"));
        posts.put(getEmptyNumber(), new Post(3, "정처기 따야되나요?", "역시 따는게 낫군여"));
    }

    private static void add() {
        System.out.print("게시물 제목을 입력해주세요 : ");
        String title = scan.nextLine();
        System.out.print("게시물 내용을 입력해주세요 : ");
        String desc = scan.nextLine();
        System.out.println("게시물이 등록되었습니다.");
        int num = getEmptyNumber();
        posts.put(num, new Post(num, title, desc));
    }

    private static void list() {
        System.out.println("==================");
        for (int i : posts.keySet()) {
            System.out.println("번호 : " + i);
            System.out.println("제목 : " + posts.get(i).getTitle());
            System.out.println("등록날짜 : " + posts.get(i).getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss")));
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
            posts.get(num).show();
            sub:
            while (who != null) {
                Integer num2 = getNumber("상세보기 기능을 선택해주세요(1. 댓글 등록, 2. 추천, 3. 수정, 4. 삭제, 5. 목록으로) : ");
                switch (num2) {
                    case 1:
                        System.out.print("댓글 내용 : ");
                        String comment = scan.nextLine();
                        posts.get(num).addComments(comment);
                        System.out.println("댓글이 성공적으로 등록되었습니다");
                        break;
                    case 2:

                        System.out.println("[추천 기능]");
                        break;
                    case 3:
                        System.out.println("[수정 기능]");
                        break;
                    case 4:
                        System.out.println("[삭제 기능]");
                        break;
                    case 5:
                        break sub;
                    default:
                        System.out.println("없는 기능입니다. 다시 선택해주세요.");
                        break;
                }
            }
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

    private static void search() {
        System.out.print("검색 키워드를 입력해주세요 : ");
        String keyword = scan.nextLine();
        List<Post> list = new ArrayList<Post>();
        for (Post post : posts.values())
            if (post.getTitle().contains(keyword))
                list.add(post);
        if (list.size() > 0)
            for (Post post : list) {
                System.out.println("번호 : " + post.getNum());
                System.out.println("제목 : " + post.getTitle());
                System.out.println("==================");
            }
        else {
            System.out.println("==================");
            System.out.println("검색 결과가 없습니다.");
            System.out.println("==================");
        }
    }

    private static void signup() {
        System.out.println("==== 회원 가입을 진행합니다 ====");
        System.out.print("아이디를 입력해주세요 : ");
        String id = scan.nextLine();
        if (members.containsKey(id)) {
            System.out.println("이미 존재하는 아이디입니다.");
            return;
        }
        System.out.print("비밀번호를 입력해주세요 : ");
        String password = scan.nextLine();
        System.out.print("닉네임을 입력해주세요 : ");
        String nickname = scan.nextLine();
        System.out.println("==== 회원 가입이 완료됐습니다 ====");
        members.put(id, new Member(id, password, nickname));
    }

    private static void login() {
        if (who != null) {
            System.out.println("이미 로그인 중입니다. 로그아웃을 시도해주세요.");
            return;
        }
        System.out.print("아이디 : ");
        String id = scan.nextLine();
        System.out.print("비밀번호 : ");
        String password = scan.nextLine();
        if (!members.containsKey(id) || !members.get(id).checkPassword(password))
            System.out.println("비밀번호를 틀렸거나 잘못된 회원정보입니다.");
        else {
            Member member = members.get(id);
            System.out.println(member.getNickname() + "님 환영합니다!");
            who = member;
        }
    }
}