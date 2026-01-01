/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import EJB.DetalleVentas.DetalleVentaFacadeLocal;
import EJB.Productos.Ventas.VentasFacadeLocal;
import Entity.Clientes;
import Entity.DetalleVenta;
import Entity.Productos;
import Entity.Ventas;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juank
 */
@SessionScoped
@Named("ventaController")
public class ventaController implements Serializable {

    @EJB
    private VentasFacadeLocal ventaFCL;
    @EJB
    private DetalleVentaFacadeLocal DetalleFCL;
    private Ventas venta;
    private String mensaje;
    private double TotalVenta;
    private List<Ventas> Listaventas;
    private DetalleVenta seleccionado;
    private Long idVentaSeleccionada;
    private Ventas ventaCabecera;
    private String cliente;
    private DetalleVenta detalleSeleccionado;
    private int cantidadTraida;
     private int cantidadNueva;

    public int getCantidadTraida() {
        return cantidadTraida;
    }

    public void setCantidadTraida(int cantidadTraida) {
        this.cantidadTraida = cantidadTraida;
    }

    public int getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(int cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }
    

    public String getCliente() {
        return cliente;
    }

    public DetalleVenta getDetalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setDetalleSeleccionado(DetalleVenta detalleSeleccionado) {
        this.detalleSeleccionado = detalleSeleccionado;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Ventas getVentaCabecera() {
        return ventaCabecera;
    }

    public void setVentaCabecera(Ventas ventaCabecera) {
        this.ventaCabecera = ventaCabecera;
    }

    public Long getIdVentaSeleccionada() {
        return idVentaSeleccionada;
    }

    public void setIdVentaSeleccionada(Long idVentaSeleccionada) {
        this.idVentaSeleccionada = idVentaSeleccionada;
    }

    private List<DetalleVenta> ListaDetalleVentas;

    public List<DetalleVenta> getListaDetalleVentas() {
        return ListaDetalleVentas;
    }

    public void setListaDetalleVentas(List<DetalleVenta> ListaDetalleVentas) {
        this.ListaDetalleVentas = ListaDetalleVentas;
    }

    public List<Ventas> getListaventas() {
        this.Listaventas = ventaFCL.obtenerVentasConCliente();
        return Listaventas;
    }

    public DetalleVenta getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(DetalleVenta seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void setListaventas(List<Ventas> Listaventas) {
        this.Listaventas = Listaventas;
    }

    public ventaController() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Double getTotalVentas() {
        Double total = ventaFCL.obtenerTotalVentas();
        return (total != null) ? total : 0.0;
    }

    @PostConstruct
    public void init() {
        venta = new Ventas();
    }

    public String detallevVenta() {
        System.out.println("entro a redireccionar");

        return "Ventas1?faces-redirect=true";
    }

    public void cargarListaDesdeUrl() {
        System.out.println("id de la venta seleccionado" + idVentaSeleccionada);
        if (idVentaSeleccionada != null) {
            this.ListaDetalleVentas = DetalleFCL.buscarPorVenta(idVentaSeleccionada);
       
            ventaCabecera = ventaFCL.find(idVentaSeleccionada);
            //this.cliente=ventaCabecventaCabeceraera.getVenta().getId_cliente().getNombre()+" "+ventaCabecera.getVenta().getId_cliente().getApellidos();
        }
        //System.out.println("total :"+ ventaCabecera.getVenta().getTotal());
     
    }

    public String prepararDetalle(Long idVenta) {

        System.out.println("id de la venta " + idVenta);
        this.ListaDetalleVentas = DetalleFCL.buscarPorVenta(idVenta);

        return "detalle_consulta?faces-redirect=true&idVenta=" + idVenta;
    }

      public void prepararEdicion(Long id,int cantidad) {
        detalleSeleccionado = new DetalleVenta();
        cantidadTraida=cantidad;
        this.detalleSeleccionado = DetalleFCL.find(id);
        System.out.println("el detalle a editar es " + detalleSeleccionado.getProducto().getNom_producto());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("detalle seleccionado es ".concat(detalleSeleccionado.getProducto().getNom_producto())));

    }
        public void editarDetalleVenta() {
        try {
            cantidadNueva=detalleSeleccionado.getCantidad();
            System.out.println("cantidad nueva "+cantidadNueva +cantidadTraida);
            DetalleFCL.edit(detalleSeleccionado);
            this.Listaventas = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cliente modificado correctamente."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo Actualizar el cliente."));
        }

    }

}
