/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class MysqlConnection implements DatabaseConnection{
    private static final String URL="";
    private static final String USER="";
    private static final String PASSWORD="";
    public static Connection conexion() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public Connection getConection() {
        try {
            return conexion();
        } catch (SQLException ex) {
            Logger.getLogger(PostgreConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
