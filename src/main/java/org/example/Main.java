package org.example;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        initial();
        main:
        while (true) {
            Member who = DataStore.getWho();
            String cmd = Input.getString("명령어를 입력해주세요" + (who != null ? "[" + who.getId() + "(" + who.getNickname() + ")]" : "") + " : ");
            switch (cmd.toLowerCase().replace(" ", "")) {
                case "exit":
                    View.sendMessage("프로그램을 종료합니다.");
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
                    View.sendMessage("로그아웃에 성공했습니다.");
                    break;
                case "test":
                    FileStore.saveData("save", true);
                    break;
                case "test2":
                    FileStore.loadData("save");
                    break;
                default:
                    View.sendMessage("없는 명령어입니다. 다시 입력해주세요.");
                    break;
            }
        }
    }


    private static void initial() {
        int num1 = DataStore.getEmptyNumber();
        DataStore.addPost(num1, new Post(num1, "안녕하세요 반갑습니다. 자바 공부중이에요.", "즐거운 자바시간"));
        int num2 = DataStore.getEmptyNumber();
        DataStore.addPost(num2, new Post(num2, "자바 질문좀 할게요~", "제곧내"));
        int num3 = DataStore.getEmptyNumber();
        DataStore.addPost(num3, new Post(num3, "정처기 따야되나요?", "역시 따는게 낫군여"));
    }

    private static void add() {
        String title = Input.getString("게시물 제목을 입력해주세요 : ");
        String desc = Input.getString("게시물 내용을 입력해주세요 : ");
        View.sendMessage("게시물이 등록되었습니다.");
        int num = DataStore.getEmptyNumber();
        DataStore.addPost(num, new Post(num, title, desc));
    }

    private static void list() {
        View.sendMessage("==================");
        for (int i : DataStore.getPostList()) {
            Post post = DataStore.getPost(i);
            View.sendMessage("번호 : " + i);
            View.sendMessage("제목 : " + post.getTitle());
            View.sendMessage("등록날짜 : " + post.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss")));
            View.sendMessage("==================");
        }
    }

    private static void update() {
        Integer num = Input.getInteger("수정할 게시물 번호 : ");
        if (num != null && DataStore.hasPost(num)) {
            String title = Input.getString("제목 : ");
            String desc = Input.getString("내용 : ");
            Post post = DataStore.getPost(num);
            post.setTitle(title);
            post.setDescription(desc);
            View.sendMessage(num + "번 게시물이 수정되었습니다.");
        } else
            View.sendMessage("없는 게시물 번호입니다.");
    }

    private static void delete() {
        Integer num = Input.getInteger("삭제할 게시물 번호 : ");
        if (DataStore.hasPost(num)) {
            View.sendMessage(num + "번 게시물이 삭제되었습니다.");
            DataStore.removePost(num);
        } else
            View.sendMessage("없는 게시물 번호입니다.");
    }

    private static void detail() {
        Integer num = Input.getInteger("상세보기 할 게시물 번호를 입력해주세요 : ");
        if (num != null && DataStore.hasPost(num)) {
            Post post = DataStore.getPost(num);
            post.show();
            Member who = DataStore.getWho();
            sub:
            while (who != null) {
                Integer num2 = Input.getInteger("상세보기 기능을 선택해주세요(1. 댓글 등록, 2. 추천, 3. 수정, 4. 삭제, 5. 목록으로) : ");
                switch (num2) {
                    case 1:
                        String comment = Input.getString("댓글 내용 : ");
                        post.addComments(comment);
                        View.sendMessage("댓글이 성공적으로 등록되었습니다");
                        break;
                    case 2:
                        View.sendMessage("[추천 기능]");
                        break;
                    case 3:
                        View.sendMessage("[수정 기능]");
                        break;
                    case 4:
                        View.sendMessage("[삭제 기능]");
                        break;
                    case 5:
                        break sub;
                    default:
                        View.sendMessage("없는 기능입니다. 다시 선택해주세요.");
                        break;
                }
            }
        } else
            View.sendMessage("없는 게시물 번호입니다.");
    }


    private static void search() {
        String keyword = Input.getString("검색 키워드를 입력해주세요 : ");
        List<Post> list = new ArrayList<Post>();
        for (Post post : DataStore.getPosts())
            if (post.getTitle().contains(keyword))
                list.add(post);
        if (list.size() > 0)
            for (Post post : list) {
                View.sendMessage("번호 : " + post.getNum());
                View.sendMessage("제목 : " + post.getTitle());
                View.sendMessage("==================");
            }
        else {
            View.sendMessage("==================");
            View.sendMessage("검색 결과가 없습니다.");
            View.sendMessage("==================");
        }
    }

    private static void signup() {
        View.sendMessage("==== 회원 가입을 진행합니다 ====");
        String id = Input.getString("아이디를 입력해주세요 : ");
        if (DataStore.hasMember(id)) {
            View.sendMessage("이미 존재하는 아이디입니다.");
            return;
        }
        String password = Input.getString("비밀번호를 입력해주세요 : ");
        String nickname = Input.getString("닉네임을 입력해주세요 : ");
        View.sendMessage("==== 회원 가입이 완료됐습니다 ====");
        DataStore.addMember(id, new Member(id, password, nickname));
    }

    private static void login() {
        if (DataStore.getWho() != null) {
            View.sendMessage("이미 로그인 중입니다. 로그아웃을 시도해주세요.");
            return;
        }
        String id = Input.getString("아이디 : ");
        String password = Input.getString("비밀번호 : ");
        if (!DataStore.hasMember(id) || !DataStore.getMember(id).checkPassword(password))
            View.sendMessage("비밀번호를 틀렸거나 잘못된 회원정보입니다.");
        else {
            Member member = DataStore.getMember(id);
            View.sendMessage(member.getNickname() + "님 환영합니다!");
            DataStore.setWho(member);
        }
    }
}