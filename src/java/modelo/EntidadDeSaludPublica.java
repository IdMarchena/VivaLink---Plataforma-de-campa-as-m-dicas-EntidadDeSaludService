/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public class EntidadDeSaludPublica extends EntidadDeSalud{
    private String codigoMinisterio;
    
    public EntidadDeSaludPublica(String nombre,
                                String direccion,
                                String telefono,
                                Usuario usuarioGerente,
                                String codigoMinisterio){
        super(nombre, direccion, telefono, usuarioGerente);
        this.codigoMinisterio=codigoMinisterio;
    }

    @Override
    public String getTipo() {
        return "publica";
    }

    /**
     * @return the codigoMinisterio
     */
    public String getCodigoMinisterio() {
        return codigoMinisterio;
    }

    /**
     * @param codigoMinisterio the codigoMinisterio to set
     */
    public void setCodigoMinisterio(String codigoMinisterio) {
        this.codigoMinisterio = codigoMinisterio;
    }
}
