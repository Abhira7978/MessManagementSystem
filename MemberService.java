import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class MemberService {

    public void addMember() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Room No: ");
        String room = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        try {
            Connection con = DBconnection.getConnection();

            String query = "INSERT INTO members (name, room_no, phone, join_date) VALUES (?, ?, ?, CURDATE())";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, room);
            ps.setString(3, phone);

            ps.executeUpdate();
            System.out.println("Member Added Successfully!");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}