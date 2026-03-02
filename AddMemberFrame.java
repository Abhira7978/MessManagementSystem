import javax.swing.*;
import java.awt.event.*;

public class AddMemberFrame extends JFrame {

    public AddMemberFrame() {

        setTitle("Add Member");
        setSize(350, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 50, 120, 25);
        add(nameField);

        JLabel roomLabel = new JLabel("Room No:");
        roomLabel.setBounds(50, 90, 100, 25);
        add(roomLabel);

        JTextField roomField = new JTextField();
        roomField.setBounds(150, 90, 120, 25);
        add(roomField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 130, 100, 25);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(150, 130, 120, 25);
        add(phoneField);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(110, 180, 100, 30);
        add(saveBtn);

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();
                String room = roomField.getText();
                String phone = phoneField.getText();

                MemberService service = new MemberService();
                service.addMemberFromGUI(name, room, phone);

                JOptionPane.showMessageDialog(null, "Member Added Successfully!");
                dispose();
            }
        });

        setVisible(true);
    }
}