import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.*;

public class ViewMemberFrame extends JFrame {

    JTextField searchField;
    JTable table;
    DefaultTableModel model;

    public ViewMemberFrame() {

        setTitle("View Members");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search by Name:"));

        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        topPanel.add(searchBtn);

        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Room");
        model.addColumn("Phone");
        model.addColumn("Join Date");

        // Double Click Listener (MUST be inside constructor)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int memberId = (int) model.getValueAt(row, 0);
                    new EditMemberFrame(memberId);
                }
            }
        });

        loadMembers("");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete Selected Member");
        add(deleteBtn, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> deleteSelectedMember());

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText();
            loadMembers(keyword);
        });

        setVisible(true);
    }

    private void loadMembers(String keyword) {

        try {
            model.setRowCount(0);

            Connection con = DBconnection.getConnection();

            String query = "SELECT * FROM members WHERE name LIKE ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("room_no"),
                        rs.getString("phone"),
                        rs.getDate("join_date")
                });
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedMember() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete.");
            return;
        }

        int memberId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Connection con = DBconnection.getConnection();

                String query = "DELETE FROM members WHERE member_id=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, memberId);

                ps.executeUpdate();
                con.close();

                loadMembers(searchField.getText());

                JOptionPane.showMessageDialog(this, "Member Deleted Successfully!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}