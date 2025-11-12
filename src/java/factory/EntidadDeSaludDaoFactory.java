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

/**
 *
 * @author Usuario
 */
public class EntidadDeSaludDaoFactory {
    public static EntidadDeSaludDao dao(String tipoDao) throws SQLException{
                switch (tipoDao.toLowerCase()) {
            case "postgre":
                return new EntidadDeSaludDaoPostgres(tipoDao);
            case "mysql":
                return new EntidadDeSaludDaoMysql();
            case "mongo":
                return new EntidadDeSaludDaoMongo();               
            default:
                throw new AssertionError();
        }
    }
}
