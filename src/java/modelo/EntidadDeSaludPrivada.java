/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public class EntidadDeSaludPrivada extends EntidadDeSalud{
        private String nitEmpresa;
    
    public EntidadDeSaludPrivada(String nombre,
                                String direccion,
                                String telefono,
                                Usuario usuarioGerente,
                                String nitEmpresa){
        super(nombre, direccion, telefono, usuarioGerente);
        this.nitEmpresa=nitEmpresa;
    }

    @Override
    public String getTipo() {
        return "privada";
    }

    /**
     * @return the codigoMinisterio
     */
    public String getNitEmpresa() {
        return nitEmpresa;
    }


    public void setNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
    }
}
