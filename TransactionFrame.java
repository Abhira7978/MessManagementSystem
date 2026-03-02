import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.*;

public class TransactionFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public TransactionFrame() {

        setTitle("All Transactions");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        model.addColumn("Member ID");
        model.addColumn("Month");
        model.addColumn("Total Amount");
        model.addColumn("Paid Amount");
        model.addColumn("Due Amount");

        loadTransactions();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadTransactions() {

        try {
            Connection con = DBconnection.getConnection();

            String query = "SELECT member_id, month, total_amount, paid_amount, due_amount FROM payments";
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