/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author juank
 */
@Entity
@Table(name="clientes")
public class Clientes {
    
    public Clientes(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
     private Long Id_Cliente;
    
    @Column(name="correo", nullable = false)
    private String correo;
    @Column(name = "direccion",nullable = false)
    private String direccion;
    @Column(name = "dui",nullable = false)
    private String dui;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_registro",nullable = false)
    private Date fecha_registro;
    @Column(name = "nombre",nullable = false)
    private String nombre;
    @Column(name = "apellidos",nullable = false)
    private String apellidos;

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    @Column(name = "telefono",nullable = false)
    private String telefono;

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
     public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Clientes other = (Clientes) obj;
        return Objects.equals(this.Id_Cliente, other.Id_Cliente);
    }

    @Override
    public String toString() {
        return "Clientes{" + "Id_Cliente=" + Id_Cliente + '}';
    }

    public Long getId_Cliente() {
        return Id_Cliente;
    }

    public void setId_Cliente(Long Id_Cliente) {
        this.Id_Cliente = Id_Cliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

 
    
}
