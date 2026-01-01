/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entity.Productos;
import java.io.Serializable;

/**
 *
 * @author juank
 */
public class ItemCarrito implements Serializable {

    private Productos producto;
    private int cantidad = 1;
    
    public ItemCarrito(Productos producto) {
        this.producto = producto;
        this.cantidad = 1; // Inicializar en 1 al agregar
    }


    public double getSubtotal() {
        return this.cantidad * this.producto.getPrecio();
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
