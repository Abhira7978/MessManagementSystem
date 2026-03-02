import java.sql.*;
import java.util.Scanner;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;


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
                generatePDFBill(memberId, month, total);

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
    public void viewPayments() {
    try {
        Connection con = DBconnection.getConnection();

        String query = "SELECT * FROM payments";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Payment Records ---");

        while (rs.next()) {
            System.out.println(
                "Member ID: " + rs.getInt("member_id") +
                " | Month: " + rs.getString("month") +
                " | Total: ₹" + rs.getDouble("total_amount") +
                " | Paid: ₹" + rs.getDouble("paid_amount") +
                " | Due: ₹" + rs.getDouble("due_amount")
            );
        }

        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void updatePayment() {
    try {
        Connection con = DBconnection.getConnection();

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Member ID: ");
        int memberId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Month (YYYY-MM): ");
        String month = sc.nextLine();

        System.out.print("Enter Paid Amount: ");
        double paid = sc.nextDouble();

        String query = "SELECT total_amount, paid_amount FROM payments WHERE member_id=? AND month=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, memberId);
        ps.setString(2, month);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            double total = rs.getDouble("total_amount");
            double alreadyPaid = rs.getDouble("paid_amount");

            double newPaid = alreadyPaid + paid;
            double due = total - newPaid;

            String updateQuery = "UPDATE payments SET paid_amount=?, due_amount=? WHERE member_id=? AND month=?";
            PreparedStatement ps2 = con.prepareStatement(updateQuery);

            ps2.setDouble(1, newPaid);
            ps2.setDouble(2, due);
            ps2.setInt(3, memberId);
            ps2.setString(4, month);

            ps2.executeUpdate();

            System.out.println("Payment Updated Successfully!");
            System.out.println("Remaining Due: ₹" + due);
        } else {
            System.out.println("No Bill Found!");
        }

        con.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void showPendingDues() {
    try {
        Connection con = DBconnection.getConnection();

        String query = "SELECT * FROM payments WHERE due_amount > 0";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Pending Dues ---");

        while (rs.next()) {
            System.out.println(
                "Member ID: " + rs.getInt("member_id") +
                " | Month: " + rs.getString("month") +
                " | Due: ₹" + rs.getDouble("due_amount")
            );
        }

        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void generatePDFBill(int memberId, String month, double total) {

    try {

        Connection con = DBconnection.getConnection();

        String memberQuery = "SELECT name, room_no FROM members WHERE member_id=?";
        PreparedStatement ps = con.prepareStatement(memberQuery);
        ps.setInt(1, memberId);
        ResultSet rs = ps.executeQuery();

        String name = "";
        String room = "";

        if (rs.next()) {
            name = rs.getString("name");
            room = rs.getString("room_no");
        }

        Document document = new Document();
        PdfWriter.getInstance(document,
                new FileOutputStream("Bill_" + memberId + "_" + month + ".pdf"));

        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

        document.add(new Paragraph("Khana Khazana Mess", titleFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Month: " + month, normalFont));
        document.add(new Paragraph("Member ID: " + memberId, normalFont));
        document.add(new Paragraph("Name: " + name, normalFont));
        document.add(new Paragraph("Room No: " + room, normalFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.addCell("Description");
        table.addCell("Amount (₹)");

        table.addCell("Monthly Total");
        table.addCell(String.valueOf(total));

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thank You For Dining With Us!", normalFont));

        document.close();
        con.close();

        System.out.println("Professional PDF Bill Generated!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}