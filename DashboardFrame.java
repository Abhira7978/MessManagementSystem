import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {

        setTitle("Khana Khazana - Mess Management System");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Title Panel
        JLabel title = new JLabel("Khana Khazana Dashboard", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(title, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton addMemberBtn = new JButton("Add Member");
        JButton viewMemberBtn = new JButton("View Members");
        JButton attendanceBtn = new JButton("Mark Attendance");
        JButton billingBtn = new JButton("Generate Bill");
        JButton paymentBtn = new JButton("Payments");
        JButton dueBtn = new JButton("View Pending Dues");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(addMemberBtn);
        buttonPanel.add(viewMemberBtn);
        buttonPanel.add(attendanceBtn);
        buttonPanel.add(billingBtn);
        buttonPanel.add(paymentBtn);
        buttonPanel.add(dueBtn);
        buttonPanel.add(exitBtn);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Button Actions
        addMemberBtn.addActionListener(e -> new AddMemberFrame());
        viewMemberBtn.addActionListener(e -> new ViewMemberFrame());
        attendanceBtn.addActionListener(e -> new AttendanceFrame());
        billingBtn.addActionListener(e -> new BillingFrame());
        paymentBtn.addActionListener(e -> new PaymentFrame());
        dueBtn.addActionListener(e -> new DueListFrame());
        exitBtn.addActionListener(e -> System.exit(0));

        // Stats Panel
JPanel statsPanel = new JPanel();
statsPanel.setLayout(new GridLayout(2, 2, 10, 10));
statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

JLabel totalMembersLabel = new JLabel();
JLabel todayAttendanceLabel = new JLabel();
JLabel totalRevenueLabel = new JLabel();
JLabel totalDueLabel = new JLabel();

statsPanel.add(totalMembersLabel);
statsPanel.add(todayAttendanceLabel);
statsPanel.add(totalRevenueLabel);
statsPanel.add(totalDueLabel);

mainPanel.add(statsPanel, BorderLayout.SOUTH);

// Load values
loadDashboardStats(totalMembersLabel, todayAttendanceLabel,
                   totalRevenueLabel, totalDueLabel);

        setVisible(true);
    }


    private void loadDashboardStats(JLabel totalMembersLabel,
                                JLabel todayAttendanceLabel,
                                JLabel totalRevenueLabel,
                                JLabel totalDueLabel) {

    try {
        Connection con = DBconnection.getConnection();

        // Total Members
        ResultSet rs1 = con.prepareStatement("SELECT COUNT(*) FROM members").executeQuery();
        if (rs1.next()) {
            totalMembersLabel.setText("Total Members: " + rs1.getInt(1));
        }

        // Today's Attendance
        ResultSet rs2 = con.prepareStatement(
                "SELECT COUNT(*) FROM attendance WHERE date = CURDATE() AND present = 1"
        ).executeQuery();
        if (rs2.next()) {
            todayAttendanceLabel.setText("Today's Present: " + rs2.getInt(1));
        }

        // Total Revenue
        ResultSet rs3 = con.prepareStatement(
                "SELECT SUM(total_amount) FROM payments"
        ).executeQuery();
        if (rs3.next()) {
            totalRevenueLabel.setText("Total Revenue: ₹" + rs3.getDouble(1));
        }

        // Total Due
        ResultSet rs4 = con.prepareStatement(
                "SELECT SUM(due_amount) FROM payments"
        ).executeQuery();
        if (rs4.next()) {
            totalDueLabel.setText("Total Pending Due: ₹" + rs4.getDouble(1));
        }

        con.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}