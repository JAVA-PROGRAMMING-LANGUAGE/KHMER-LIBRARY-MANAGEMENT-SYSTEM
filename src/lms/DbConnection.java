/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author SRUN VANNARA
 */
public class DbConnection {
    private static String url = "jdbc:postgresql://localhost/postgres";
    private static String user = "postgres";
    private static String pwd = "8899";
        
    public static Connection connect() {
        Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, user, pwd);
            } catch (SQLException ex) {
                new MyDialog().showInfoDialog("មានបញ្ហា!", ex.getMessage());
        }
            return conn;
        }
    
}
