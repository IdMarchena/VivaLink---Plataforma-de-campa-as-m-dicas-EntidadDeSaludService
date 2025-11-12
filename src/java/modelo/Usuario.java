/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author Usuario
 */
public class Usuario {
    private int id;
    private String telefono;
    private int salarioBase;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;

    public Usuario(int id, String telefono, int SalarioBase, LocalDate fechaNacimient, String sexo, String direccion) {
        this.id = id;
        this.telefono = telefono;
        this.salarioBase = SalarioBase;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.direccion = direccion;
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
     * @return the SalarioBase
     */
    public int getSalarioBase() {
        return salarioBase;
    }

    /**
     * @param SalarioBase the SalarioBase to set
     */
    public void setSalarioBase(int SalarioBase) {
        this.salarioBase = SalarioBase;
    }

    /**
     * @return the fechaNacimient
     */
    public LocalDate getFechaNacimient() {
        return fechaNacimiento;
    }

    /**
     * @param fechaNacimient the fechaNacimient to set
     */
    public void setFechaNacimient(LocalDate fechaNacimient) {
        this.fechaNacimiento = fechaNacimient;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
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
    
    
    
}
