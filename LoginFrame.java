import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Mess Management Login");
        setSize(350, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 50, 120, 25);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 90, 100, 25);
        add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 90, 120, 25);
        add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 140, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String username = userField.getText();
                String password = new String(passField.getPassword());

                LoginService loginService = new LoginService();

                if (loginService.validateLogin(username, password)) {
                    dispose();
                    new DashboardFrame();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
    public boolean validateLogin(String username, String password) {
    try {
        Connection con = DBconnection.getConnection();

        String query = "SELECT * FROM admin WHERE username=? AND password=SHA2(?,256)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        return rs.next();

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}