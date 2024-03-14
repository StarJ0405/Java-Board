package org.example;


import org.example.DBSystem.DBStore;

import javax.xml.crypto.Data;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        DataStore.getDb().loadAllData();
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
                    list(DataStore.getPosts().stream().toList());
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
                    detail(Input.getInteger("상세보기 할 게시물 번호를 입력해주세요 : "));
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
                    View.sendMessage("sort - 게시물 정렬");
                    View.sendMessage("search - 게시물 찾기");
                    View.sendMessage("detail - 상세보기");
                    if (who != null) {
                        View.sendMessage("add - 게시물 추가");
                        if (who.isAdmin()) {
                            View.sendMessage("update - 게시물 수정");
                            View.sendMessage("delete - 게시물 삭제");
                            View.sendMessage("transfer - 저장 방식 변경");
                        }
                        View.sendMessage("logout - 로그아웃");
                    }
                    View.sendMessage("signup - 회원가입");
                    View.sendMessage("login - 로그인");
                    View.sendMessage("exit - 프로그램 종료");
                    break;
                case "sort":
                    sort();
                    break;
                case "page":
                    page();
                    break;
                case "transfer":
                    if (who != null && who.isAdmin())
                        transfer();
                    else View.sendConsoleErrorMessage();
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
        DataStore.getDb().setPost(num + "", post); // 추가
    }

    private static void list(List<Post> list) {
        View.sendMessage("==================");
        for (Post post : list) {
            View.sendMessage("번호 : " + post.getNum());
            View.sendMessage("제목 : " + post.getTitle());
            View.sendMessage("작성자 : " + DataStore.getMember(post.getAuthor()).getNickname());
            View.sendMessage("조회수 : " + post.getShow());
            View.sendMessage("좋아요 : " + post.getLoves().size());
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
            DataStore.getDb().setPost(num + "", post); // 수정
            View.sendMessage(num + "번 게시물이 수정되었습니다.");
        } else View.sendMessage("없는 게시물 번호입니다.");
    }

    private static void delete(Integer num) {
        if (num != null && DataStore.hasPost(num)) {
            Boolean check = Input.getBoolean("정말 게시물을 삭제하시겠습니까? (y/n) : ", "y", "n", true);
            if (check != null && check) {
                View.sendMessage(num + "번 게시물이 삭제되었습니다.");
                DataStore.removePost(num);
                DataStore.getDb().setPost(num + "", null); // 제거
            } else View.sendMessage("게시글 삭제가 취소되었습니다.");
        } else View.sendMessage("없는 게시물 번호입니다.");
    }

    private static void detail(Integer num) {
        if (num != null && DataStore.hasPost(num)) {
            Post post = DataStore.getPost(num);
            Member who = DataStore.getWho();
            post.show(who);
            DataStore.getDb().setPost(num + "", post); // 조회수 변경
            sub:
            while (who != null) {
                Integer num2 = Optional.ofNullable(Input.getInteger("상세보기 기능을 선택해주세요(1. 댓글 등록, 2. 좋아요, 3. 수정, 4. 삭제, 5. 목록으로) : ")).orElse(-1);
                switch (num2) {
                    case 1:
                        String comment = Input.getString("댓글 내용(공백 입력시 취소) : ");
                        if (comment.equals("")) {
                            View.sendMessage("댓글 입력이 취소되었습니다.");
                            break;
                        }
                        post.addComments(who.getID(), comment);
                        View.sendMessage("댓글이 성공적으로 등록되었습니다");
                        detail(num);
                        break sub;
                    case 2:
                        if (post.hasLoves(who)) {
                            post.removeLoves(who);
                            View.sendMessage("해당 게시물의 좋아요를 해제합니다.");
                        } else {
                            post.addLoves(who);
                            View.sendMessage("해당 게시물을 좋아합니다.");
                        }
                        DataStore.getDb().setPost(num + "", post);
                        detail(num);
                        break sub;
                    case 3:
                        if (post.getAuthor().equals(who.getID())) {
                            update(num);
                            detail(num);
                            break sub;
                        } else View.sendMessage("자신의 게시물만 수정 할 수 있습니다.");
                        break;
                    case 4:
                        if (post.getAuthor().equals(who.getID())) {
                            delete(num);
                            break sub;
                        } else View.sendMessage("자신의 게시물만 삭제 할 수 있습니다.");
                        break;
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
        if (id.length() > 25) {
            View.sendMessage("아이디는 25자 이내만 가능합니다.");
            return;
        }

        String password = Input.getString("비밀번호를 입력해주세요 : ");
        String nickname = Input.getString("닉네임을 입력해주세요 : ");
        if (nickname.length() > 25) {
            View.sendMessage("닉네임은 25자 이내만 가능합니다.");
            return;
        }

        View.sendMessage("==== 회원 가입이 완료됐습니다 ====");
        Member member = new Member(id, password, nickname);
        DataStore.addMember(id, member);
        DataStore.getDb().setMember(id, member);
    }

    private static void login() {
        if (DataStore.getWho() != null) {
            View.sendMessage("이미 로그인 중입니다. 로그아웃을 먼저 시도해주세요.");
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

    public static void sort() {
        int target = Input.getInteger("정렬 대상을 선택해주세요. (1. 번호,  2. 조회수) : ");
        int how = Input.getInteger("정렬 방법을 선택해주세요. (1. 오름차순,  2. 내림차순) : ");
        List<Post> list = new ArrayList<Post>();
        list.addAll(DataStore.getPosts().stream().toList());
        switch (target) {
            case 1:
                Collections.sort(list, new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return (o1.getNum() > o2.getNum() ? 1 : -1) * (how == 1 ? 1 : -1);
                    }
                });
                break;
            case 2:
                Collections.sort(list, new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return (o1.getShow() > o2.getShow() ? 1 : -1) * (how == 1 ? 1 : -1);
                    }
                });
                break;
        }
        list(list);
    }

    private static void page() {
        List<Post> list = DataStore.getPosts().stream().toList();
        // 3 단위
        int maxPage = list.size() / 3 + (list.size() % 3 != 0 ? 1 : 0);
        int page = DataStore.getPage();
        int start = page / 5;
        int end = Math.min(maxPage, start + 5);
        System.out.println(maxPage);
        sub:
        while (true) {
            list(list.subList(page * 3, Math.min(list.size(), (page + 1) * 3)));
            String msg = "";
            for (int i = start; i < end; i++)
                msg += (i == page ? "[" + (i + 1) + "]" : (i + 1)) + " ";
            msg += ">>";
            View.sendMessage(msg);
            Integer pagingCommand = Optional.ofNullable(Input.getInteger("페이징 명령어를 입력해주세요 ((1. 이전, 2. 다음, 3. 선택, 4. 뒤로가기): ")).orElse(-1);
            switch (pagingCommand) {
                case 1:
                    page = Math.max(0, page - 1);
                    DataStore.setPage(page);
                    break;
                case 2:
                    page = Math.min(maxPage - 1, page + 1);
                    DataStore.setPage(page);
                    break;
                case 3:
                    Integer newPaging = Input.getInteger("이동하실 페이지 번호를 입력해주세요 : ");
                    if (newPaging != null && (newPaging - 1) >= 0 && (newPaging - 1) <= maxPage - 1) {
                        page = newPaging - 1;
                        DataStore.setPage(page);
                    } else
                        View.sendMessage("없는 페이지입니다.");
                    break;
                case 4:
                    break sub;

            }
        }
    }

    private static void transfer() {
        View.sendMessage("현재 저장 방식 : " + DataStore.getDb().getSaveType());
        Boolean value = Input.getBoolean("변경하시겠습니까? (y/n) : ", "y", "n", true);
        if (value != null && value) {
            DBStore db = DataStore.changeDb();
            db.transferedFromOthers();
            View.sendMessage("성공적으로 변경되었습니다.");
        } else
            View.sendMessage("변경이 취소되었습니다.");
    }
}