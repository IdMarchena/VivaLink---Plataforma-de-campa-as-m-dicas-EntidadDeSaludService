/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;
import dao.EntidadDeSaludDao;
import factory.EntidadDeSaludDaoFactory;
import java.sql.SQLException;
import java.util.List;
import modelo.EntidadDeSalud;
import modelo.Usuario;
/**
 *
 * @author Usuario
 */
public class EntidadDeSaludRepository {
    private final EntidadDeSaludDao entidadDeSaludDao;
    public EntidadDeSaludRepository() throws SQLException{    
        entidadDeSaludDao = EntidadDeSaludDaoFactory.dao("postgres");
    }
    
    public EntidadDeSalud buscarEntidadDeSaludPorId(int id) {
        return entidadDeSaludDao.buscarEntidadDeSaludPorId(id);
    }
    
    public boolean verificarSiEntidadDeSaludExiste(int id) {
        return entidadDeSaludDao.verificarSiEntidadDeSaludExiste(id);
    }
    public void guardar(EntidadDeSalud entidadDeSalud, String identificador) {
        entidadDeSaludDao.guardar(entidadDeSalud, identificador);
    }
    public List<EntidadDeSalud> listarTodasLasEntidadDeSalud() {
        return entidadDeSaludDao.listarTodasLasEntidadDeSalud();
    }
    
    public void actualizarEntidadDeSalud(int id, EntidadDeSalud entidadDeSalud, String tipo) {
        entidadDeSaludDao.actualizarEntidadDeSalud(id, entidadDeSalud, tipo);
    }
    
    public void eliminarEntidadDeSalud(int id) {
        entidadDeSaludDao.eliminarEntidadDeSalud(id);
    }
    public List<EntidadDeSalud> buscarEntidadDeSaludPorNombre(String tipo){
        return entidadDeSaludDao.buscarEntidadDeSaludPorNombre(tipo);
    }
    
    public List<EntidadDeSalud> buscarEntidadDeSaludPorDireccion(String tipo){
        return entidadDeSaludDao.buscarEntidadDeSaludPorDireccion(tipo);
    }

    public List<EntidadDeSalud> buscarEntidadDeSaludPorGerente(int idUsuarioGerente) {
        return entidadDeSaludDao.buscarEntidadDeSaludPorGerente(idUsuarioGerente);
    }
    
    public Usuario buscarUsuarioPorIdEntidadDeSalud(int id){
        return entidadDeSaludDao.buscarUsuarioPorIdEntidadDeSalud(id);
    }

    
}
