/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package murach.sql;

import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;
/**
 *
 * @author ADMIN
 */
public class ConnectionPool {
    private static ConnectionPool pool = null;
    private static DataSource dataSource = null;

    private ConnectionPool() {
        try {
            Context envContext = (Context) new InitialContext().lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/murach");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void freeConnection(Connection c) throws SQLException {
        c.close();
    }
}
