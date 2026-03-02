import javax.swing.*;
import java.sql.*;
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

public class AttendanceChartFrame extends JFrame {

    public AttendanceChartFrame() {

        setTitle("Monthly Attendance Chart");
        setSize(600, 400);
        setLocationRelativeTo(null);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection con = DBconnection.getConnection();

            String query = """
                    SELECT DATE_FORMAT(date, '%Y-%m') AS month,
                           COUNT(*) AS total_present
                    FROM attendance
                    WHERE present = 1
                    GROUP BY DATE_FORMAT(date, '%Y-%m')
                    """;

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dataset.addValue(
                        rs.getInt("total_present"),
                        "Present Days",
                        rs.getString("month")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Monthly Attendance",
                "Month",
                "Total Present Days",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);

        setVisible(true);
    }
}