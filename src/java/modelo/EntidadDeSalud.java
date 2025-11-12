/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public abstract class EntidadDeSalud implements Cloneable{
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private Usuario usuarioGerente;
    public EntidadDeSalud(String nombre, 
                          String direccion,
                          String telefono,
                          Usuario usuarioGerente){
        this.nombre=nombre;
        this.direccion=direccion;
        this.telefono=telefono;
        this.usuarioGerente=usuarioGerente;
    }
    public abstract String getTipo();
    
    @Override
    public EntidadDeSalud clone(){
        try{
            return (EntidadDeSalud) super.clone();
        } catch (CloneNotSupportedException e){
            throw new RuntimeException("Error clonando entidad de salud"+e);
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the usuarioGerente
     */
    public Usuario getUsuarioGerente() {
        return usuarioGerente;
    }

    /**
     * @param usuarioGerente the usuarioGerente to set
     */
    public void setUsuarioGerente(Usuario usuarioGerente) {
        this.usuarioGerente = usuarioGerente;
    }
    
    
}
