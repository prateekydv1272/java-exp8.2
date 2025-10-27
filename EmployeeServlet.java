import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/companydb";
    private static final String USER = "root";
    private static final String PASS = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empIdParam = request.getParameter("empid");

        out.println("<html><body style='font-family: Arial; text-align: center;'>");
        out.println("<h2>Employee Details</h2>");

        try {
            // Step 1: Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish connection
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement ps;
            ResultSet rs;

            // Step 3: Check if user searched for specific employee
            if (empIdParam != null && !empIdParam.isEmpty()) {
                int empId = Integer.parseInt(empIdParam);
                ps = con.prepareStatement("SELECT * FROM Employee WHERE EmpID = ?");
                ps.setInt(1, empId);
            } else {
                ps = con.prepareStatement("SELECT * FROM Employee");
            }

            rs = ps.executeQuery();

            // Step 4: Display result
            out.println("<table border='1' align='center' cellpadding='8' style='border-collapse: collapse;'>");
            out.println("<tr style='background-color: #dcdcdc;'><th>EmpID</th><th>Name</th><th>Salary</th></tr>");

            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getDouble("Salary") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            if (!found) {
                out.println("<p style='color: red;'>No employee found with ID: " + empIdParam + "</p>");
            }

            // Step 5: Close resources
            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }

        out.println("<br><a href='employee.html'>Back to Search</a>");
        out.println("</body></html>");
    }
}
