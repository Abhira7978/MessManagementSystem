import java.sql.*;
import java.util.Scanner;

public class AttendanceService {

    Scanner sc = new Scanner(System.in);

    public void markAttendance() {
        try {
            Connection con = DBconnection.getConnection();

            System.out.print("Enter Member ID: ");
            int memberId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            System.out.print("Is Present? (true/false): ");
            boolean present = sc.nextBoolean();

            boolean breakfast = false;
            boolean lunch = false;
            boolean dinner = false;

            if (present) {
                System.out.print("Breakfast Taken? (true/false): ");
                breakfast = sc.nextBoolean();

                System.out.print("Lunch Taken? (true/false): ");
                lunch = sc.nextBoolean();

                System.out.print("Dinner Taken? (true/false): ");
                dinner = sc.nextBoolean();
            }

            String query = "INSERT INTO attendance (member_id, date, breakfast, lunch, dinner, present) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, memberId);
            ps.setString(2, date);
            ps.setBoolean(3, breakfast);
            ps.setBoolean(4, lunch);
            ps.setBoolean(5, dinner);
            ps.setBoolean(6, present);

            ps.executeUpdate();

            System.out.println("Attendance Marked Successfully!");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}