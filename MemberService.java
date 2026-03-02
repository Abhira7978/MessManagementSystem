import java.sql.*;
import java.util.Scanner;

public class MemberService {

    Scanner sc = new Scanner(System.in);

    // 1️⃣ Add Member
    public void addMember() {
        try {
            Connection con = DBconnection.getConnection();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Room No: ");
            String room = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

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

    // 2️⃣ View Members
    public void viewMembers() {
        try {
            Connection con = DBconnection.getConnection();

            String query = "SELECT * FROM members";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Member List ---");
            while (rs.next()) {
                System.out.println(
                        "ID: " + rs.getInt("member_id") +
                        " | Name: " + rs.getString("name") +
                        " | Room: " + rs.getString("room_no") +
                        " | Phone: " + rs.getString("phone") +
                        " | Join Date: " + rs.getDate("join_date")
                );
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3️⃣ Delete Member
    public void deleteMember() {
        try {
            Connection con = DBconnection.getConnection();

            System.out.print("Enter Member ID to Delete: ");
            int id = sc.nextInt();
            sc.nextLine();

            String query = "DELETE FROM members WHERE member_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Member Deleted Successfully!");
            else
                System.out.println("Member Not Found!");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4️⃣ Update Member
    public void updateMember() {
        try {
            Connection con = DBconnection.getConnection();

            System.out.print("Enter Member ID to Update: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Name: ");
            String name = sc.nextLine();

            System.out.print("Enter New Room No: ");
            String room = sc.nextLine();

            System.out.print("Enter New Phone: ");
            String phone = sc.nextLine();

            String query = "UPDATE members SET name=?, room_no=?, phone=? WHERE member_id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, room);
            ps.setString(3, phone);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Member Updated Successfully!");
            else
                System.out.println("Member Not Found!");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addMemberFromGUI(String name, String room, String phone) {
    try {
        Connection con = DBconnection.getConnection();

        String query = "INSERT INTO members (name, room_no, phone, join_date) VALUES (?, ?, ?, CURDATE())";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, name);
        ps.setString(2, room);
        ps.setString(3, phone);

        ps.executeUpdate();
        con.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}