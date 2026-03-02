import javax.swing.*;
import java.sql.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;

public class RevenueChartFrame extends JFrame {

    public RevenueChartFrame() {

        setTitle("Monthly Revenue Chart");
        setSize(600, 400);
        setLocationRelativeTo(null);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection con = DBconnection.getConnection();
            ResultSet rs = con.prepareStatement(
                "SELECT month, SUM(total_amount) as revenue FROM payments GROUP BY month"
            ).executeQuery();

            while (rs.next()) {
                dataset.addValue(
                        rs.getDouble("revenue"),
                        "Revenue",
                        rs.getString("month")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Monthly Revenue",
                "Month",
                "Revenue (₹)",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);

        setVisible(true);
    }
}