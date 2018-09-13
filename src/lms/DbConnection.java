/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author SRUN VANNARA
 */
public class DbConnection {
    private final String url="jdbc:postgresql://localhost/postgres";
        private final String user="postgres";
        private final String pwd="8899";
        
        public java.sql.Connection connect(){
            java.sql.Connection conn=null;
            try {
                conn=DriverManager.getConnection(url, user, pwd);
                //System.out.println("Connected");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            return conn;
        }
    
}
