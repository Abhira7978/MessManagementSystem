import java.sql.*;
import java.util.Scanner;

public class BillingService {

    Scanner sc = new Scanner(System.in);

    public void generateMonthlyBill() {

        try {
            Connection con = DBconnection.getConnection();

            System.out.print("Enter Member ID: ");
            int memberId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Month (YYYY-MM): ");
            String month = sc.nextLine();

            String query = """
                    SELECT 
                        SUM(breakfast) AS totalBreakfast,
                        SUM(lunch) AS totalLunch,
                        SUM(dinner) AS totalDinner,
                        SUM(present) AS totalPresent
                    FROM attendance
                    WHERE member_id = ?
                    AND DATE_FORMAT(date, '%Y-%m') = ?
                    """;

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, memberId);
            ps.setString(2, month);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int breakfast = rs.getInt("totalBreakfast");
                int lunch = rs.getInt("totalLunch");
                int dinner = rs.getInt("totalDinner");
                int present = rs.getInt("totalPresent");

                double total =
                        (breakfast * 20) +
                        (lunch * 50) +
                        (dinner * 30) +
                        (present * 10);

                System.out.println("\n----- Monthly Bill -----");
                System.out.println("Breakfast Count: " + breakfast);
                System.out.println("Lunch Count: " + lunch);
                System.out.println("Dinner Count: " + dinner);
                System.out.println("Present Days: " + present);
                System.out.println("Total Amount: ₹" + total);

                String insertPayment = """
                        INSERT INTO payments (member_id, month, total_amount, paid_amount, due_amount)
                        VALUES (?, ?, ?, 0, ?)
                        """;

                PreparedStatement ps2 = con.prepareStatement(insertPayment);
                ps2.setInt(1, memberId);
                ps2.setString(2, month);
                ps2.setDouble(3, total);
                ps2.setDouble(4, total);

                ps2.executeUpdate();

                System.out.println("Bill Stored Successfully!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}