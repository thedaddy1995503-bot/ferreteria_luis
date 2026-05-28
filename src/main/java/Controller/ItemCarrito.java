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
    private double cantidad = 1;
    private boolean esVentaPorUnidad = false;

    private Double precioVentaManual = null;

    public ItemCarrito(Productos producto) {
        this.producto = producto;
        this.cantidad = 1; // Inicializar en 1 al agregar
    }

    public double getSubtotal() {
        return this.cantidad * getPrecioAplicado();
    }

    public void setSubtotal(double nuevoSubtotal) {
        if (this.cantidad > 0) {
            setPrecioAplicado(nuevoSubtotal / this.cantidad);
        }
    }

    public double getPrecioAplicado() {
        if (precioVentaManual != null) {
            return precioVentaManual;
        }
        Double pUnit = this.producto.getPrecio_unitario();
        Double pQuintal = this.producto.getPrecio();
        return esVentaPorUnidad ? (pUnit != null ? pUnit : 0.0) : (pQuintal != null ? pQuintal : 0.0);
    }

    public void setPrecioAplicado(double precioAplicado) {
        this.precioVentaManual = precioAplicado;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        // Forzar a entero (eliminando decimales) antes de guardar
        this.cantidad = (double) ((long) cantidad);
    }

    public boolean isEsVentaPorUnidad() {
        return esVentaPorUnidad;
    }

    public void setEsVentaPorUnidad(boolean esVentaPorUnidad) {
        this.esVentaPorUnidad = esVentaPorUnidad;
    }

}
