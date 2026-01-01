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
import java.io.Serializable;

/**
 *
 * @author juank
 */
@Entity 
@Table(name="productos")
public class Productos implements Serializable{

    public Productos() {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long Id_Producto;
    @Column(name = "nombre",nullable = false)
    private String nom_producto;
    @Column(name="stock",nullable = false)
    private Long stock;
    @Column(name="precio",nullable = false)
    private Double precio;
    @Column(name="categoria")
    private String Categoria;
    @Column(name="descripcion")
    private String Descripcion;

   // public Productos(Long id_Producto, String nom_producto, Double precio, Long stock) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    //}

    @Override
    public String toString() {
        return "Productos{" + "Id_Producto=" + Id_Producto + '}';
    }

    public Long getId_Producto() {
        return Id_Producto;
    }

    public void setId_Producto(Long Id_Producto) {
        this.Id_Producto = Id_Producto;
    }

    public String getNom_producto() {
        return nom_producto;
    }

    public void setNom_producto(String nom_producto) {
        this.nom_producto = nom_producto;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Object getProducto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
    
    
    
    
}
