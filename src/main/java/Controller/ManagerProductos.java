/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import EJB.DetalleVentas.DetalleVentaFacadeLocal;
import EJB.Productos.ProductosFacadeLocal;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.primefaces.PrimeFaces;

/**
 *
 * @author juank
 */
@SessionScoped
@Named("ManagerProductos")
public class ManagerProductos implements Serializable {

    @EJB
    private ProductosFacadeLocal ProductoFCL;
    @EJB
    private VentasFacadeLocal ventasFCD;
    @EJB
    private DetalleVentaFacadeLocal detalleventaFL;

    private DetalleVenta detalleventa;
    private Clientes cliente;
    private static final double IVA_RATE = 0.13; // 13% de IVA
    private List<Productos> ListaProductos;
    private Productos ProdSeleccionado;
    private Productos cantidadProd;
    private Productos productos;
    private ManagerClientes manaCliente;
    private Productos productoMenor;
    private Ventas ventas;
    private String mensaje;
    private String codigoBusqueda;
    private List<Productos> resultadosBusqueda;
    private List<ItemCarrito> itemsCarrito;
    private Clientes idC;
    private String nomClientes = " ";

    public String getNomClientes() {
        return nomClientes;
    }

    public void setNomClientes(String nomClientes) {
        this.nomClientes = nomClientes;
    }

    public Productos getProductoMenor() {
        return productoMenor;
    }

    public void setProductoMenor(Productos productoMenor) {
        this.productoMenor = productoMenor;
    }

    public List<Productos> getResultadosBusqueda() {
        return resultadosBusqueda;
    }

    public void setResultadosBusqueda(List<Productos> resultadosBusqueda) {
        this.resultadosBusqueda = resultadosBusqueda;
    }

    public List<ItemCarrito> getItemsCarrito() {
        return itemsCarrito;
    }

    public void setItemsCarrito(List<ItemCarrito> itemsCarrito) {
        this.itemsCarrito = itemsCarrito;
    }

    public String getCodigoBusqueda() {
        return codigoBusqueda;
    }

    public void setCodigoBusqueda(String codigoBusqueda) {
        this.codigoBusqueda = codigoBusqueda;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    public List<Productos> getListaProductos() {
        this.ListaProductos = ProductoFCL.findAll();
        return ListaProductos;
    }

    public void setListaProductos(List<Productos> ListaProductos) {
        this.ListaProductos = ListaProductos;
    }

    public Productos getProdSeleccionado() {
        return ProdSeleccionado;
    }

    public void setProdSeleccionado(Productos ProdSeleccionado) {
        this.ProdSeleccionado = ProdSeleccionado;
    }

    @PostConstruct
    public void init() {
        productos = new Productos();
        this.itemsCarrito = new ArrayList<>();
        this.nomClientes = "";
        this.resultadosBusqueda = ProductoFCL.listarPrimeros10();
        this.productoMenor = ProductoFCL.obtenerProductoConMenorInventario();
    }

    public String guardarProducto() {
        try {
            System.out.println("Entro a guardar producto");
            ProductoFCL.create(productos);
            this.mensaje = "Producto '" + productos.getNom_producto() + "' capturado. ¡Lógica de guardado pendiente!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto guardado correctamente", null));
            productos = new Productos(); // limpiar formulario
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar cliente", e.getMessage()));
        }
        return null;
    }

    public void eliminarCliente(Object id) {

        try {
            productos = ProductoFCL.find(id);
            System.out.println("el producto a borrar es " + productos.getNom_producto());
            ProductoFCL.remove(productos);
            this.ListaProductos = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cliente eliminado."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el cliente."));
        }

    }

    public void prepararEdicion(Long id) {
        ProdSeleccionado = new Productos();
        this.ProdSeleccionado = ProductoFCL.find(id);
        System.out.println("el cliente a editar es " + ProdSeleccionado.getNom_producto());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("cliente seleccionado es ".concat(ProdSeleccionado.getNom_producto())));

    }

    public void editarProducto() {
        try {
            ProductoFCL.edit(ProdSeleccionado);
            this.ListaProductos = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "producto modificado correctamente."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo Actualizar el producto."));
        }

    }

    public void buscarProducto() {

        this.resultadosBusqueda = new ArrayList<>();
        //this.resultadosBusqueda = new ArrayList<>();
        if (codigoBusqueda == null || codigoBusqueda.trim().length() < 3) {
            // Limpia la lista si el texto es muy corto o vacío
            this.resultadosBusqueda = ProductoFCL.listarPrimeros10();
            return;
        }

        try {
            List<Productos> productosEntidad = ProductoFCL.BuscarPorNombreParcial(codigoBusqueda.trim());
            // --- Mapeo de Entidad JPA (Productos) a POJO Frontend (Producto) ---
            //this.resultadosBusqueda = new ArrayList<>();
            System.out.println("codigo busqueda " + codigoBusqueda);
            for (Productos entidad : productosEntidad) {
                System.out.println("entro al for " + entidad);
                this.resultadosBusqueda.add(mapEntidadToPojo(entidad));
            }
            // --- Fin Mapeo ---

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo buscar el productio." + e.getMessage()));
            System.out.println("error exception " + e.getMessage());

        }
    }

    private Productos mapEntidadToPojo(Productos entidad) {
        // Asume que Productos tiene métodos getID, getNom_producto, etc.
        Productos pojo = new Productos();
        pojo.setId_Producto(entidad.getId_Producto()); // Usando los nombres de tu tabla (image_4f4883.png)
        pojo.setNom_producto(entidad.getNom_producto());  // 'nombre' de la columna (image_4f4883.png)
        pojo.setPrecio(entidad.getPrecio());  // 'precio' de la columna (image_4f4883.png)
        pojo.setStock(entidad.getStock());
        return pojo;
    }

    // --- Método Auxiliar para Mensajes de JSF ---
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    public void agregarAlCarrito(Productos productoAAgregar) {
        System.out.println("ENTRO A AGREGAR PRODUCTO");
        Optional<ItemCarrito> itemExistente = itemsCarrito.stream()
                .filter(item -> item.getProducto().getId_Producto().equals(productoAAgregar.getId_Producto()))
                .findFirst();

        if (itemExistente.isPresent()) {
            // Si ya está en el carrito, solo incrementa la cantidad
            itemExistente.get().setCantidad(itemExistente.get().getCantidad() + 1);
            addMessage(FacesMessage.SEVERITY_INFO, "Info", productoAAgregar.getNom_producto() + " (Cantidad incrementada).");
        } else {
            // Si es nuevo, agrégalo
            itemsCarrito.add(new ItemCarrito(productoAAgregar));
            addMessage(FacesMessage.SEVERITY_INFO, "Info", productoAAgregar.getNom_producto() + " agregado al carrito.");
        }

        // Confirma en la consola que la lista creció:
        System.out.println("Producto añadido. Total de items en carrito: " + this.itemsCarrito.size());
        // Limpiar resultados después de agregar (opcional)
        //this.resultadosBusqueda = null;
        //this.codigoBusqueda = "";
    }

    public void eliminarItem(ItemCarrito item) {
        itemsCarrito.remove(item);

        addMessage(FacesMessage.SEVERITY_INFO, "Info", "Producto eliminado del carrito.");
        System.out.println("producto borrado ");
    }

    public void finalizarVenta() {
        if (itemsCarrito.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "El carrito está vacío. Agregue productos.");
            return;
        }

        // 1. Verificar stock (IMPORTANTE: Lógica de negocio)
        System.out.println("id cliente #" + idC);
        //id_cliente = Math.toIntExact(idC);

        verificarStock();

        // 3. Llamar al Facade/Microservicio para persistir y decrementar stock.
        // --- SIMULACIÓN ---
        System.out.println("Venta finalizada. Total: " + getTotalVenta());
        addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "¡Venta registrada exitosamente! Total: $" + String.format("%.2f", getTotalVenta()));

        this.itemsCarrito = new ArrayList<>();
        this.resultadosBusqueda = ProductoFCL.listarPrimeros10();
        this.itemsCarrito.clear(); // Limpiar carrito
        //return "dashboard?faces-redirect=true";
        PrimeFaces.current().executeScript("PF('dlgVentaExitosa').show();");
        // --- FIN SIMULACIÓN ---
        idC = new Clientes();
        nomClientes = "";
    }

    public void verificarStock() {
        cantidadProd = new Productos();
        for (int i = 0; i < itemsCarrito.size(); i++) {
            ItemCarrito item = itemsCarrito.get(i);
            Long id_item;
            if (i == 0) {
                System.out.println("Producto: " + item.getProducto().getNom_producto());
                System.out.println("Id: " + item.getProducto().getId_Producto());
                id_item = item.getProducto().getId_Producto();
                System.out.println("Cantidad: " + item.getCantidad());
                System.out.println("Precio unitario: $" + item.getSubtotal());
                System.out.println("Subtotal: $" + item.getSubtotal());
                System.out.println("---------------------------");
                cantidadProd = ProductoFCL.find(id_item);
                LocalDateTime fechaVenta = LocalDateTime.now();
                Timestamp fecha = Timestamp.valueOf(fechaVenta);

                ventas = new Ventas();
                ventas.setId_cliente(idC);
                ventas.setFecha_venta(new Date());
                System.out.println("fecha " + fecha);
                ventas.setTotal(getTotalVenta());
                System.out.println("cantidad inventario" + item.getCantidad() + " y cantidad stock" + cantidadProd.getStock());
                if (item.getCantidad() <= cantidadProd.getStock()) {
                    int cantidad = item.getCantidad();
                    System.out.println("ventas " + ventas.getFecha_venta());
                    System.out.println("ventas " + ventas.getTotal() + " y " + ventas.getFecha_venta());
                    System.out.println("ventas " + ventas.getId_cliente());

                    ventasFCD.create(ventas);

                    ProductoFCL.actualizarStock(id_item, cantidad);

                } else {
                    System.out.println("producto agotado");
                    PrimeFaces.current().executeScript("PF('dlgAgotado'.show())");
                    return;
                }

            }
            // 2. Crear objeto Venta (venta y Detalle_venta)
            System.out.println("ventas" + ventas.getTotal());
            //ventasFCD.create(ventas);
            int id_venta;
            id_venta = ventasFCD.obtenerUltimoIdVenta();
            detalleventa = new DetalleVenta();
            System.out.println("Item #" + (i + 1));
            System.out.println("Producto ID: " + item.getProducto().getId_Producto());
            System.out.println("Cantidad: " + item.getCantidad());
            System.out.println("Subtotal: $" + item.getSubtotal());
            detalleventa.setVenta(ventas);
            detalleventa.setCantidad(item.getCantidad());
            detalleventa.setProducto(item.getProducto());
            //detalleventa.setId_producto(Math.toIntExact(item.getProducto().getId_Producto()));
            detalleventa.setPrecio_unitario(item.getProducto().getPrecio());
            double subtotalReal = item.getCantidad() * item.getProducto().getPrecio();
            // Redondear a 2 decimales antes de setearlo
            subtotalReal = Math.round(subtotalReal * 100.0) / 100.0;
            System.out.println("Subtotal real : $" + item.getSubtotal());
            detalleventa.setSubtotal(subtotalReal);
            detalleventaFL.create(detalleventa);

        }

    }

    public void idCliente(Clientes c) {
        System.out.println("cliente " + c.getId_Cliente());
        idC = c;
        nomClientes = c.getNombre();
        System.out.println("cliente seleccionado " + nomClientes);
        //searchResultList-cli cliente carritoTable totalSummary
    }

    public double getSubtotalVenta() {
        return itemsCarrito.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    public double getTotalVenta() {
        return getSubtotalVenta() + getImpuestoVenta();
    }

    public double getImpuestoVenta() {
        return getSubtotalVenta() * IVA_RATE;
    }

    public Double getTotalVentas() {
        Double total = ventasFCD.obtenerTotalVentas();
        return (total != null) ? total : 0.0;
    }

}
