import java.sql.*;
import java.util.Scanner;

public class LoginService {

    Scanner sc = new Scanner(System.in);

    public boolean login() {

        try {
            Connection con = DBconnection.getConnection();

            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            String query = "SELECT * FROM admin WHERE username=? AND password=SHA2(?,256)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nLogin Successful (Secure Mode)!\n");
                return true;
            } else {
                System.out.println("\nInvalid Credentials!\n");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}