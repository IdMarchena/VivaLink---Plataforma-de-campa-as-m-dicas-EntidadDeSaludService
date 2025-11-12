/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package dto;
import dto.UsuarioDto;
/**
 *
 * @author Usuario
 */
public record EntidadDeSaludDto(String tipo,
                                String nombre,
                                String direccion,
                                String telefono,
                                String identificador,
                                UsuarioDto usuarioDto
                                ) {

}
