/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import modelo.EntidadDeSalud;
import modelo.Usuario;
/**
 *
 * @author Usuario
 */
public interface EntidadDeSaludDao {
    EntidadDeSalud buscarEntidadDeSaludPorId(int id);
    boolean verificarSiEntidadDeSaludExiste(int id);
    void guardar(EntidadDeSalud usuario,String tipo);
    List<EntidadDeSalud> buscarEntidadDeSaludPorNombre(String tipo);
    List<EntidadDeSalud> listarTodasLasEntidadDeSalud();
    void actualizarEntidadDeSalud(int id, EntidadDeSalud usuario, String tipo);
    void eliminarEntidadDeSalud(int id);
    List<EntidadDeSalud> buscarEntidadDeSaludPorDireccion(String tipo);
    List<EntidadDeSalud> buscarEntidadDeSaludPorGerente(int id);
    Usuario buscarUsuarioPorIdEntidadDeSalud(int id);
}
