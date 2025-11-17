/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio.service;
import dto.EntidadDeSaludDto;
import dto.UsuarioDto;
import java.util.List;
/**
 *
 * @author Usuario
 */
public interface EntidadDeSaludService {
    
    void crearEntidadDeSalud(EntidadDeSaludDto entidadDto);
    void actualizarEntidadDeSalud(int id, EntidadDeSaludDto entidadDto);
    void eliminarEntidadDeSalud(int id);
    EntidadDeSaludDto buscarEntidadDeSalud(int id);
    
    List<EntidadDeSaludDto> listarEntidadeDeSalud();
    
    List<EntidadDeSaludDto> listarEntidadDeSaludPorTipo(String tipo);
    List<EntidadDeSaludDto> listarEntidadDeSaludPorDireccion(String direccion);
    
    List<EntidadDeSaludDto> buscarEntidadDeSaludPorGerente(int idUsuarioGerente);
    
    boolean existeEntidadConNombre(String nombre);
    int contarEntidadesPorTipo(String tipo);
    
    void registrarEntidadConUsuario(EntidadDeSaludDto entidadDto);
    
    UsuarioDto buscarUsuarioPorIdEntidadDeSalud(int id);
    
}
