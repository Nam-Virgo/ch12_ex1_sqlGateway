package murach.sql;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import murach.sql.ConnectionPool;

import java.sql.*;

public class SqlGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String message = "";
        String sqlResult = "";

        if (sqlStatement == null || sqlStatement.trim().isEmpty()) {
            message = "Please enter an SQL statement.";
        } else {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection connection = null;

            try {
                connection = pool.getConnection();
                Statement statement = connection.createStatement();

                sqlStatement = sqlStatement.trim();

                if (sqlStatement.toLowerCase().startsWith("select")) {
                    // SELECT → trả kết quả bảng HTML
                    ResultSet resultSet = statement.executeQuery(sqlStatement);
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                    resultSet.close();
                    message = "Query executed successfully.";
                } else {
                    // INSERT, UPDATE, DELETE, DDL...
                    int count = statement.executeUpdate(sqlStatement);
                    if (count == 0) {
                        message = "The statement executed successfully.";
                    } else {
                        message = "The statement executed successfully.<br>" +
                                  count + " row(s) affected.";
                    }
                }

                statement.close();
            } catch (SQLException e) {
                message = "Error executing the SQL statement:<br>" + e.getMessage();
            } finally {
                if (connection != null) {
                    try {
                        pool.freeConnection(connection); // ✅ trả connection về pool
                    } catch (SQLException e) {
                        // ghi log, không làm gián đoạn servlet
                        e.printStackTrace();
                    }
                }
            }
        }

        // ✅ Lưu kết quả cho JSP hiển thị
        HttpSession session = request.getSession();
        session.setAttribute("sqlStatement", sqlStatement);
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("message", message);

        // ✅ Forward về index.jsp
        String url = "/index.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {
        doPost(request, response);
    }
}