/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;
import dao.EntidadDeSaludDao;
import dao.EntidadDeSaludDaoMongo;
import dao.EntidadDeSaludDaoMysql;
import dao.EntidadDeSaludDaoPostgres;
import java.sql.SQLException;
import dao.conexion.DatabaseConnection;

/**
 *
 * @author Usuario
 */
public class EntidadDeSaludDaoFactory {
    public static EntidadDeSaludDao dao(String tipoDao) throws SQLException{
        DatabaseConnection conn = DatabaseConnectionFactory.connection(tipoDao);
                switch (tipoDao.toLowerCase()) {
            case "postgres" -> {
                return new EntidadDeSaludDaoPostgres(conn);
            }
            case "mysql" -> {
                return new EntidadDeSaludDaoMysql(conn);
            }
            case "mongo" -> {
                return new EntidadDeSaludDaoMongo(conn);
            }
            default -> throw new AssertionError();
        }
    }
}
