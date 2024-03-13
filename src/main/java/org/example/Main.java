package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        FileStore.loadAllData();
        main:
        while (true) {
            Member who = DataStore.getWho();
            String cmd = Input.getString("명령어를 입력해주세요" + (who != null ? "[" + who.getNickname() + (who.isAdmin() ? "(관리자)" : "") + "]" : "") + " : ");
            switch (cmd.toLowerCase().replace(" ", "")) {
                case "exit":
                    View.sendMessage("프로그램을 종료합니다.");
                    break main;
                case "add":
                    if (who != null) add();
                    else View.sendConsoleErrorMessage();
                    break;
                case "list":
                    list();
                    break;
                case "update":
                    if (who != null && who.isAdmin()) update(Input.getInteger("수정할 게시물 번호 : "));
                    else View.sendConsoleErrorMessage();
                    break;
                case "delete":
                    if (who != null && who.isAdmin()) delete(Input.getInteger("삭제할 게시물 번호 : "));
                    else View.sendConsoleErrorMessage();
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
                    if (who != null) {
                        DataStore.setWho(null);
                        View.sendMessage("로그아웃에 성공했습니다.");
                    } else View.sendConsoleErrorMessage();
                    break;
                case "help":
                    View.sendMessage("list - 게시물 목록");
                    View.sendMessage("search - 게시물 찾기");
                    View.sendMessage("detail - 상세보기");
                    if (who != null) {
                        View.sendMessage("add - 게시물 추가");
                        if (who.isAdmin()) {
                            View.sendMessage("update - 게시물 수정");
                            View.sendMessage("delete - 게시물 삭제");
                        }
                        View.sendMessage("logout - 로그아웃");
                    }
                    View.sendMessage("signup - 회원가입");
                    View.sendMessage("login - 로그인");
                    View.sendMessage("exit - 프로그램 종료");
                    break;
                default:
                    View.sendConsoleErrorMessage();
                    break;
            }
        }
    }


    private static void add() {
        String title = Input.getString("게시물 제목을 입력해주세요(공백입력시 취소) : ");
        if (title.equals("")) {
            View.sendMessage("게시글 수정이 취소되었습니다.");
            return;
        }
        String desc = Input.getString("게시물 내용을 입력해주세요(공백입력시 취소) : ");
        if (desc.equals("")) {
            View.sendMessage("게시글 수정이 취소되었습니다.");
            return;
        }
        View.sendMessage("게시물이 등록되었습니다.");
        int num = DataStore.getEmptyNumber();
        Post post = new Post(num, DataStore.getWho().getID(), title, desc);
        DataStore.addPost(num, post);
        FileStore.setPost(num + "", post); // 추가
    }

    private static void list() {
        View.sendMessage("==================");
        for (int i : DataStore.getPostList()) {
            Post post = DataStore.getPost(i);
            View.sendMessage("번호 : " + i);
            View.sendMessage("제목 : " + post.getTitle());
            View.sendMessage("작성일 : " + post.getDate().format(View.getDateTimeFormatter()));
            View.sendMessage("작성자 : " + DataStore.getMember(post.getAuthor()).getNickname());
            View.sendMessage("==================");
        }
    }

    private static void update(Integer num) {
        if (num != null && DataStore.hasPost(num)) {
            String title = Input.getString("제목(공백입력시 기존값 유지) : ");
            String desc = Input.getString("내용(공백입력시 기존값 유지) : ");
            Post post = DataStore.getPost(num);
            if (!title.equals(""))
                post.setTitle(title);
            if (!desc.equals(""))
                post.setDescription(desc);
            FileStore.setPost(num + "", post); // 수정
            View.sendMessage(num + "번 게시물이 수정되었습니다.");
        } else View.sendMessage("없는 게시물 번호입니다.");
    }

    private static void delete(Integer num) {
        if (num != null && DataStore.hasPost(num)) {
            Boolean check = Input.getBoolean("정말 게시물을 삭제하시겠습니까? (y/n) : ", "y", "n", true);
            if (check != null && check) {
                View.sendMessage(num + "번 게시물이 삭제되었습니다.");
                DataStore.removePost(num);
                FileStore.setPost(num + "", null); // 제거
            } else View.sendMessage("게시글 삭제가 취소되었습니다.");
        } else View.sendMessage("없는 게시물 번호입니다.");
    }

    private static void detail() {
        Integer num = Input.getInteger("상세보기 할 게시물 번호를 입력해주세요 : ");
        if (num != null && DataStore.hasPost(num)) {
            Post post = DataStore.getPost(num);
            post.show();
            FileStore.setPost(num + "", post); // 조회수 변경
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
                        if (post.getAuthor().equals(who.getID())) update(num);
                        else View.sendMessage("자신의 게시물만 수정 할 수 있습니다.");
                        break;
                    case 4:
                        if (post.getAuthor().equals(who.getID())) delete(num);
                        else View.sendMessage("자신의 게시물만 삭제 할 수 있습니다.");
                        break sub;
                    case 5:
                        break sub;
                    default:
                        View.sendMessage("없는 기능입니다. 다시 선택해주세요.");
                        break;
                }
            }
        } else View.sendMessage("없는 게시물 번호입니다.");
    }


    private static void search() {
        String keyword = Input.getString("검색 키워드를 입력해주세요 : ");
        List<Post> list = new ArrayList<Post>();
        for (Post post : DataStore.getPosts())
            if (post.getTitle().contains(keyword)) list.add(post);
        if (list.size() > 0) for (Post post : list) {
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
        Member member = new Member(id, password, nickname);
        DataStore.addMember(id, member);
        FileStore.setMember(id, member);
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