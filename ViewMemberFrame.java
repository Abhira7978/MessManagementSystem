import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewMemberFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewMemberFrame() {

        setTitle("View Members");
        setSize(600, 400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Room");
        model.addColumn("Phone");
        model.addColumn("Join Date");

        loadMembers();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setVisible(true);
    }

    private void loadMembers() {

        try {
            Connection con = DBconnection.getConnection();
            String query = "SELECT * FROM members";
            PreparedStatement ps = con.prepareStatement(query);
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
}