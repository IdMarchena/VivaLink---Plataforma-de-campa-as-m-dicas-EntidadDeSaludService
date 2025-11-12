/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package dto;

import java.time.LocalDate;

/**
 *
 * @author Usuario
 */
public record UsuarioDto(int id, 
                         String telefono, 
                         int SalarioBase, 
                         LocalDate fechaNacimient, 
                         String sexo, 
                         String direccion ) {

}
