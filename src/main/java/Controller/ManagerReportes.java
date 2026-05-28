package Controller;

import EJB.DetalleVentas.DetalleVentaFacadeLocal;
import EJB.Productos.Ventas.VentasFacadeLocal;
import Entity.DetalleVenta;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Controlador para la gestión de reportes financieros y de productos.
 * 
 * @author juank
 */
@Named("ManagerReportes")
@SessionScoped
public class ManagerReportes implements Serializable {

    @EJB
    private VentasFacadeLocal ventasFCD;
    @EJB
    private DetalleVentaFacadeLocal detalleVentaFCD;

    private List<DetalleVenta> listaDetalles;
    private Double totalVentas;
    private Double totalGanancias;
    private Date fechaInicio;
    private Date fechaFin;

    public ManagerReportes() {
    }

    @PostConstruct
    public void init() {
        Calendar cal = Calendar.getInstance();
        fechaFin = cal.getTime(); // Hoy

        cal.set(Calendar.DAY_OF_MONTH, 1); // Primer día del mes
        fechaInicio = cal.getTime();

        cargarReportes();
    }

    public void cargarReportes() {
        this.listaDetalles = detalleVentaFCD.obtenerPorRangoFechas(fechaInicio, fechaFin);
        calcularTotales();
    }

    private void calcularTotales() {
        this.totalVentas = 0.0;
        this.totalGanancias = 0.0;

        if (listaDetalles != null) {
            for (DetalleVenta d : listaDetalles) {
                this.totalVentas += d.getSubtotal();
                this.totalGanancias += calcularGananciaItem(d);
            }
        }

        // Redondear a 2 decimales
        this.totalVentas = Math.round(this.totalVentas * 100.0) / 100.0;
        this.totalGanancias = Math.round(this.totalGanancias * 100.0) / 100.0;
    }

    public Double calcularGananciaItem(DetalleVenta d) {
        if (d == null || d.getProducto() == null)
            return 0.0;

        Entity.Productos p = d.getProducto();
        Double pCompraBulk = (p.getPrecio_compra() != null) ? p.getPrecio_compra() : 0.0;
        Double costEfectivo = pCompraBulk;

        // Si el producto tiene factor de conversión (ej. Hierro)
        if (p.getUnidadxmedida() != null && p.getUnidadxmedida() > 0) {
            // Heurística: ¿Se vendió por UNIDAD?
            Double pVentaUnidad = (p.getPrecio_unitario() != null) ? p.getPrecio_unitario() : 0.0;

            // Si el precio de venta es igual al precio unitario, calculamos costo por
            // unidad
            if (Math.abs(d.getPrecio_unitario() - pVentaUnidad) < 0.01) {
                costEfectivo = pCompraBulk / p.getUnidadxmedida();
            }
        }

        return (d.getPrecio_unitario() - costEfectivo) * d.getCantidad();
    }

    // Getters y Setters
    public List<DetalleVenta> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleVenta> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    public Double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Double getTotalGanancias() {
        return totalGanancias;
    }

    public void setTotalGanancias(Double totalGanancias) {
        this.totalGanancias = totalGanancias;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
