import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AttendanceFrame extends JFrame {

    JComboBox<Integer> memberBox;
    JCheckBox presentBox, breakfastBox, lunchBox, dinnerBox;

    public AttendanceFrame() {

        setTitle("Mark Attendance");
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel memberLabel = new JLabel("Select Member:");
        memberLabel.setBounds(50, 40, 120, 25);
        add(memberLabel);

        memberBox = new JComboBox<>();
        memberBox.setBounds(180, 40, 150, 25);
        add(memberBox);

        loadMembers();

        presentBox = new JCheckBox("Present");
        presentBox.setBounds(50, 80, 100, 25);
        add(presentBox);

        breakfastBox = new JCheckBox("Breakfast");
        breakfastBox.setBounds(50, 120, 100, 25);
        add(breakfastBox);

        lunchBox = new JCheckBox("Lunch");
        lunchBox.setBounds(50, 160, 100, 25);
        add(lunchBox);

        dinnerBox = new JCheckBox("Dinner");
        dinnerBox.setBounds(50, 200, 100, 25);
        add(dinnerBox);

        JButton saveBtn = new JButton("Save Attendance");
        saveBtn.setBounds(120, 250, 150, 30);
        add(saveBtn);

        saveBtn.addActionListener(e -> saveAttendance());

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

    private void saveAttendance() {

        try {
            Connection con = DBconnection.getConnection();

            int memberId = (int) memberBox.getSelectedItem();
            boolean present = presentBox.isSelected();
            boolean breakfast = breakfastBox.isSelected();
            boolean lunch = lunchBox.isSelected();
            boolean dinner = dinnerBox.isSelected();

            String query = "INSERT INTO attendance (member_id, date, breakfast, lunch, dinner, present) VALUES (?, CURDATE(), ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, memberId);
            ps.setBoolean(2, breakfast);
            ps.setBoolean(3, lunch);
            ps.setBoolean(4, dinner);
            ps.setBoolean(5, present);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Attendance Saved!");
            con.close();
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}