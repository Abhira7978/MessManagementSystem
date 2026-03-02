import javax.swing.*;
import java.sql.*;

public class EditMemberFrame extends JFrame {

    int memberId;
    JTextField nameField, roomField, phoneField;

    public EditMemberFrame(int memberId) {

        this.memberId = memberId;

        setTitle("Edit Member");
        setSize(350, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 50, 120, 25);
        add(nameField);

        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setBounds(50, 90, 100, 25);
        add(roomLabel);

        roomField = new JTextField();
        roomField.setBounds(150, 90, 120, 25);
        add(roomField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 130, 100, 25);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(150, 130, 120, 25);
        add(phoneField);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(110, 180, 100, 30);
        add(updateBtn);

        loadMemberData();

        updateBtn.addActionListener(e -> updateMember());

        setVisible(true);
    }

    private void loadMemberData() {
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM members WHERE member_id=?"
            );
            ps.setInt(1, memberId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                roomField.setText(rs.getString("room_no"));
                phoneField.setText(rs.getString("phone"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMember() {
        try {
            Connection con = DBconnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE members SET name=?, room_no=?, phone=? WHERE member_id=?"
            );

            ps.setString(1, nameField.getText());
            ps.setString(2, roomField.getText());
            ps.setString(3, phoneField.getText());
            ps.setInt(4, memberId);

            ps.executeUpdate();
            con.close();

            JOptionPane.showMessageDialog(this, "Member Updated!");
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}