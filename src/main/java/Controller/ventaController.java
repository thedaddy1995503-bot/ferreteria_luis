/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import EJB.DetalleVentas.DetalleVentaFacadeLocal;
import EJB.Productos.Ventas.VentasFacadeLocal;
import Entity.DetalleVenta;
import Entity.Ventas;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.faces.context.ExternalContext;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
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
    private List<Ventas> listaVentas;
    private DetalleVenta seleccionado;
    private Long idVentaSeleccionada;
    private Ventas ventaCabecera;
    private String cliente;
    private DetalleVenta detalleSeleccionado;
    private double cantidadTraida;
    private double cantidadNueva;

    public double getCantidadTraida() {
        return cantidadTraida;
    }

    public void setCantidadTraida(double cantidadTraida) {
        this.cantidadTraida = cantidadTraida;
    }

    public double getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(double cantidadNueva) {
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

    private List<DetalleVenta> listaDetalleVentas;

    public List<DetalleVenta> getListaDetalleVentas() {
        return listaDetalleVentas;
    }

    public void setListaDetalleVentas(List<DetalleVenta> listaDetalleVentas) {
        this.listaDetalleVentas = listaDetalleVentas;
    }

    public List<Ventas> getListaVentas() {
        this.listaVentas = ventaFCL.obtenerVentasConCliente();
        return listaVentas;
    }

    public DetalleVenta getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(DetalleVenta seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void setListaVentas(List<Ventas> listaVentas) {
        this.listaVentas = listaVentas;
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
        System.out.println("DEBUG: Cargando lista desde URL. idVentaSeleccionada = " + idVentaSeleccionada);
        if (idVentaSeleccionada != null) {
            try {
                this.ventaCabecera = ventaFCL.find(idVentaSeleccionada);
                if (this.ventaCabecera != null) {
                    System.out
                            .println("DEBUG: Venta encontrada. Cliente: " + ventaCabecera.getId_cliente().getNombres());
                    this.listaDetalleVentas = DetalleFCL.buscarPorVenta(idVentaSeleccionada);
                    System.out.println("DEBUG: Detalles cargados: "
                            + (listaDetalleVentas != null ? listaDetalleVentas.size() : 0));
                } else {
                    System.err.println("ERROR: No se encontró la venta con ID " + idVentaSeleccionada);
                }
            } catch (Exception e) {
                System.err.println("EXCEPCIÓN al cargar detalles: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public String prepararDetalle(Long idVenta) {

        System.out.println("id de la venta " + idVenta);
        this.listaDetalleVentas = DetalleFCL.buscarPorVenta(idVenta);
        this.ventaCabecera = ventaFCL.find(idVenta);

        return "detalle_consulta?faces-redirect=true&idVenta=" + idVenta;
    }

    public void prepararEdicion(Long id, double cantidad) {
        detalleSeleccionado = new DetalleVenta();
        cantidadTraida = cantidad;
        this.detalleSeleccionado = DetalleFCL.find(id);
        System.out.println("el detalle a editar es " + detalleSeleccionado.getProducto().getNom_producto());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                "detalle seleccionado es ".concat(detalleSeleccionado.getProducto().getNom_producto())));

    }

    public void editarDetalleVenta() {
        try {
            cantidadNueva = detalleSeleccionado.getCantidad();
            System.out.println("cantidad nueva " + cantidadNueva + cantidadTraida);
            DetalleFCL.edit(detalleSeleccionado);
            this.listaVentas = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Detalle modificado correctamente."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el detalle."));
        }

    }

    public void actualizarVenta(Ventas v) {
        try {
            ventaFCL.edit(v);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Estado de venta actualizado."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "No se pudo actualizar el estado de la venta."));
        }
    }

    public void togglePagado(Ventas v) {
        v.setPagado(!v.isPagado());
        actualizarVenta(v);
    }

    public void toggleEntregado(Ventas v) {
        v.setEntregado(!v.isEntregado());
        actualizarVenta(v);
    }

    public void actualizarDetalle(DetalleVenta d) {
        try {
            DetalleFCL.edit(d);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Estado del detalle actualizado."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "No se pudo actualizar el estado del detalle."));
        }
    }

    public String prepararTicket(Ventas v) {
        if (v == null) {
            return null;
        }
        this.ventaCabecera = v;
        this.listaDetalleVentas = DetalleFCL.buscarPorVenta(v.getId_venta());
        return "ImprimirTicket?faces-redirect=true";
    }

    public void exportarPDF(Ventas v) {
        if (v == null) {
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try {
            // 1. Configurar respuesta para descarga de archivo
            externalContext.responseReset();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Content-Disposition",
                    "attachment; filename=\"Presupuesto_" + v.getId_venta() + ".pdf\"");

            OutputStream out = externalContext.getResponseOutputStream();

            // 2. Crear documento PDF
            Document documento = new Document(PageSize.A4);
            PdfWriter.getInstance(documento, out);

            documento.open();

            // --- ESTILOS ---
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font fontHeaderTabla = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);

            // --- ENCABEZADO ---
            Paragraph pTitulo = new Paragraph("materiales de construccion y transporte MARTINEZ AGUILAR", fontTitulo);
            pTitulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(pTitulo);

            Paragraph pContacto = new Paragraph("Teléfono: 7209 8154 | Email: luismartinezaguilar49@gmail.com",
                    fontSubtitulo);
            pContacto.setAlignment(Element.ALIGN_CENTER);
            documento.add(pContacto);

            Paragraph pDireccion = new Paragraph("Dirección: Canton el paraiso turin ahuachapan", fontSubtitulo);
            pDireccion.setAlignment(Element.ALIGN_CENTER);
            pDireccion.setSpacingAfter(20);
            documento.add(pDireccion);

            Paragraph pTipo = new Paragraph(v.isPagado() ? "COMPROBANTE DE VENTA" : "PRESUPUESTO DE PRODUCTOS",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(0, 102, 204)));
            pTipo.setAlignment(Element.ALIGN_CENTER);
            pTipo.setSpacingAfter(15);
            documento.add(pTipo);

            // --- DATOS DEL CLIENTE ---
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            infoTable.addCell(createCellNoBorder(
                    "Cliente: " + v.getId_cliente().getNombres() + " " + v.getId_cliente().getApellidos(), fontLabel));
            infoTable.addCell(createCellNoBorder(
                    "Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(v.getFecha_venta()), fontLabel));
            infoTable.addCell(createCellNoBorder("DUI: " + v.getId_cliente().getDui(), fontValue));
            infoTable.addCell(createCellNoBorder("ID Venta: #" + v.getId_venta(), fontValue));

            documento.add(infoTable);

            // --- DETALLE DE PRODUCTOS ---
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            try {
                tabla.setWidths(new float[] { 4f, 1.5f, 1.5f, 2f });
            } catch (Exception e) {
            }

            // Encabezados
            addHeaderCell(tabla, "Producto", fontHeaderTabla);
            addHeaderCell(tabla, "Cant.", fontHeaderTabla);
            addHeaderCell(tabla, "P. Unit", fontHeaderTabla);
            addHeaderCell(tabla, "Subtotal", fontHeaderTabla);

            List<DetalleVenta> detalles = DetalleFCL.buscarPorVenta(v.getId_venta());
            if (detalles != null) {
                for (DetalleVenta d : detalles) {
                    tabla.addCell(new Paragraph(d.getProducto().getNom_producto(), fontValue));
                    tabla.addCell(new Paragraph(String.valueOf(d.getCantidad()), fontValue));
                    tabla.addCell(new Paragraph("$" + String.format("%.2f", d.getPrecio_unitario()), fontValue));
                    tabla.addCell(new Paragraph("$" + String.format("%.2f", d.getSubtotal()), fontValue));
                }
            }

            documento.add(tabla);

            // --- TOTAL ---
            Paragraph pTotal = new Paragraph("\nTOTAL A PAGAR: $" + String.format("%.2f", v.getTotal()),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(0, 102, 204)));
            pTotal.setAlignment(Element.ALIGN_RIGHT);
            documento.add(pTotal);

            documento.close();
            out.flush();
            out.close();
            facesContext.responseComplete();

        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private com.lowagie.text.pdf.PdfPCell createCellNoBorder(String text, Font font) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new Paragraph(text, font));
        cell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        cell.setPadding(5);
        return cell;
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(new Color(0, 102, 204));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        table.addCell(cell);
    }
}
