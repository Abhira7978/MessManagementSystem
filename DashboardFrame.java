import javax.swing.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {

        setTitle("Mess Management Dashboard");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label = new JLabel("Welcome to Khana Khazana");
        label.setBounds(100, 50, 250, 30);
        add(label);

        JButton addMemberBtn = new JButton("Add Member");
        addMemberBtn.addActionListener(e -> new AddMemberFrame());
        addMemberBtn.setBounds(120, 100, 150, 30);
        add(addMemberBtn);

        JButton viewMemberBtn = new JButton("View Members");
        viewMemberBtn.setBounds(120, 140, 150, 30);
        add(viewMemberBtn);

        viewMemberBtn.addActionListener(e -> new ViewMemberFrame());

        JButton attendanceBtn = new JButton("Mark Attendance");
        attendanceBtn.setBounds(120, 180, 150, 30);
        add(attendanceBtn);

        attendanceBtn.addActionListener(e -> new AttendanceFrame());


        JButton billingBtn = new JButton("Generate Bill");
        billingBtn.setBounds(120, 220, 150, 30);
        add(billingBtn);

        billingBtn.addActionListener(e -> new BillingFrame());

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(120, 270, 150, 30);
        add(exitBtn);

        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}