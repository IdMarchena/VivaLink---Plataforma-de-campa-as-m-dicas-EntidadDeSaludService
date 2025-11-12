/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;
import dao.conexion.DatabaseConnection;
import modelo.Usuario;
import modelo.EntidadDeSaludPublica;
import modelo.EntidadDeSaludPrivada;
import modelo.EntidadDeSalud;
import dao.conexion.MongoConnection;
import dao.conexion.MysqlConnection;
import dao.conexion.PostgreConnection;
/**
 *
 * @author Usuario
 */
public class EntidadDeSaludFactory {
    public static EntidadDeSalud crearEntidad(String tipo,
                                                     String nombre,
                                                     String direccion,
                                                     String telefono,
                                                     Usuario usuarioGerente,
                                                     String identificador){
        return switch (tipo.toLowerCase()){
            case "publica" -> new EntidadDeSaludPublica(nombre,direccion,telefono,usuarioGerente,identificador);
            case "privada" -> new EntidadDeSaludPrivada(nombre,direccion,telefono,usuarioGerente,identificador);
            default -> throw new IllegalArgumentException("tipo de entidad no soportado"+tipo);         
        };
    }
}
