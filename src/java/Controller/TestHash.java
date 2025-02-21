package Controller;

/**
 *
 * @author le minh khoa
 */
public class TestHash {
    public static void main(String[] args) {
        String password = "admin123"; // Đổi mật khẩu tại đây
        String hashedPassword = MyUtils.hashPassword(password);
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
