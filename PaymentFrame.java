import javax.swing.*;
import java.sql.*;

public class PaymentFrame extends JFrame {

    JComboBox<Integer> memberBox;
    JTextField monthField, amountField;
    JTextArea resultArea;

    public PaymentFrame() {

        setTitle("Update Payment");
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

        JLabel monthLabel = new JLabel("Month (YYYY-MM):");
        monthLabel.setBounds(40, 70, 150, 25);
        add(monthLabel);

        monthField = new JTextField();
        monthField.setBounds(180, 70, 150, 25);
        add(monthField);

        JLabel amountLabel = new JLabel("Enter Paid Amount:");
        amountLabel.setBounds(40, 110, 150, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(180, 110, 150, 25);
        add(amountField);

        JButton payBtn = new JButton("Update Payment");
        payBtn.setBounds(120, 150, 170, 30);
        add(payBtn);

        resultArea = new JTextArea();
        resultArea.setBounds(40, 200, 350, 130);
        add(resultArea);

        payBtn.addActionListener(e -> updatePayment());

        setVisible(true);
    }

    private void loadMembers() {
        try {
            Connection con = DBconnection.getConnection();
            ResultSet rs = con.prepareStatement("SELECT member_id FROM members").executeQuery();
            while (rs.next()) {
                memberBox.addItem(rs.getInt("member_id"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePayment() {
        try {
            Connection con = DBconnection.getConnection();

            int memberId = (int) memberBox.getSelectedItem();
            String month = monthField.getText();
            double paid = Double.parseDouble(amountField.getText());

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

                resultArea.setText(
                        "Total: ₹" + total + "\n" +
                        "Paid: ₹" + newPaid + "\n" +
                        "Remaining Due: ₹" + due
                );

            } else {
                resultArea.setText("No bill found for this member & month!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}