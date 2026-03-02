import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class BillingFrame extends JFrame {

    JComboBox<Integer> memberBox;
    JTextField monthField;
    JTextArea resultArea;

    public BillingFrame() {

        setTitle("Generate Monthly Bill");
        setSize(450, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel memberLabel = new JLabel("Select Member:");
        memberLabel.setBounds(40, 30, 120, 25);
        add(memberLabel);

        memberBox = new JComboBox<>();
        memberBox.setBounds(180, 30, 150, 25);
        add(memberBox);

        loadMembers();

        JLabel monthLabel = new JLabel("Enter Month (YYYY-MM):");
        monthLabel.setBounds(40, 70, 150, 25);
        add(monthLabel);

        monthField = new JTextField();
        monthField.setBounds(180, 70, 150, 25);
        add(monthField);

        JButton generateBtn = new JButton("Generate Bill");
        generateBtn.setBounds(130, 110, 150, 30);
        add(generateBtn);

        resultArea = new JTextArea();
        resultArea.setBounds(40, 160, 350, 170);
        add(resultArea);

        generateBtn.addActionListener(e -> generateBill());

        setVisible(true);
    }

    private void loadMembers() {
        try {
            Connection con = DBconnection.getConnection();
            String query = "SELECT member_id FROM members";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                memberBox.addItem(rs.getInt("member_id"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateBill() {

        try {
            Connection con = DBconnection.getConnection();

            int memberId = (int) memberBox.getSelectedItem();
            String month = monthField.getText();

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

                resultArea.setText(
                        "Breakfast Count: " + breakfast + "\n" +
                        "Lunch Count: " + lunch + "\n" +
                        "Dinner Count: " + dinner + "\n" +
                        "Present Days: " + present + "\n\n" +
                        "Total Amount: ₹" + total
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}