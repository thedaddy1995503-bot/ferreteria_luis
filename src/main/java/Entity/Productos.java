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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author juank
 */
@Entity
@Table(name = "productos")
public class Productos implements Serializable {

    public Productos() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id_producto;
    @Column(name = "nombre", nullable = false)
    private String nom_producto;
    @Column(name = "stock", nullable = false)
    private Double stock;
    @Column(name = "precio", nullable = false)
    private Double precio;
    @Column(name = "categoria")
    private String Categoria;
    @Column(name = "descripcion")
    private String Descripcion;
    @Column(name = "precio_compra")
    private Double precio_compra;
    @ManyToOne
    @JoinColumn(name = "id_medida")
    private Medida medida;
    @Column(name = "precio_unitario")
    private Double precio_unitario;
    @Column(name = "unidadxmedida")
    private Integer unidadxmedida;

    // public Productos(Long id_Producto, String nom_producto, Double precio, Long
    // stock) {
    // throw new UnsupportedOperationException("Not supported yet."); // Generated
    // from
    // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    // }

    @Override
    public String toString() {
        return "Productos{" + "id_producto=" + id_producto + '}';
    }

    public Long getId_producto() {
        return id_producto;
    }

    public void setId_producto(Long id_producto) {
        this.id_producto = id_producto;
    }

    public String getNom_producto() {
        return nom_producto;
    }

    public void setNom_producto(String nom_producto) {
        this.nom_producto = nom_producto;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
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

    public Double getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(Double precio_compra) {
        this.precio_compra = precio_compra;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Integer getUnidadxmedida() {
        return unidadxmedida;
    }

    public void setUnidadxmedida(Integer unidadxmedida) {
        this.unidadxmedida = unidadxmedida;
    }

    public Object getProducto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
