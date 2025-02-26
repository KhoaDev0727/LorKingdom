package Controller;

/**
 *
 * @author le minh khoa
 */
public class TestHash {
//    public static void main(String[] args) {
//        String password = "staff123"; // Đổi mật khẩu tại đây
//        String hashedPassword = MyUtils.hashPassword(password);
//        System.out.println("Hashed Password: " + hashedPassword);
//    }

    public static void main(String[] args) {
        String firstHash = "10176e7b7b24d317acfcf8d2064cfd2f24e154f7b5a96603077d5ef813d6a6b6";
        String secondHash = MyUtils.hashPassword(firstHash);
        System.out.println("Hash of first hash: " + secondHash);
    }

}
