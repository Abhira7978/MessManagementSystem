import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DueListFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public DueListFrame() {

        setTitle("Pending Dues");
        setSize(600, 400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("Member ID");
        model.addColumn("Month");
        model.addColumn("Total Amount");
        model.addColumn("Paid Amount");
        model.addColumn("Due Amount");

        loadDues();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setVisible(true);
    }

    private void loadDues() {
        try {
            Connection con = DBconnection.getConnection();

            String query = "SELECT * FROM payments WHERE due_amount > 0";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("member_id"),
                        rs.getString("month"),
                        rs.getDouble("total_amount"),
                        rs.getDouble("paid_amount"),
                        rs.getDouble("due_amount")
                });
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}