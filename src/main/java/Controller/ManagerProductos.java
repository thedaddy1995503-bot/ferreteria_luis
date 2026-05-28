/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import EJB.DetalleVentas.DetalleVentaFacadeLocal;
import EJB.MedidaFacadeLocal;
import EJB.Productos.ProductosFacadeLocal;
import EJB.Productos.Ventas.VentasFacadeLocal;
import Entity.Clientes;
import Entity.DetalleVenta;
import Entity.Medida;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import java.awt.Color;

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
    @EJB
    private MedidaFacadeLocal medidaFCL;

    @jakarta.inject.Inject
    private ManagerMedida managerMedida;

    private DetalleVenta detalleventa;
    // private Clientes cliente; // Eliminado
    private static final double IVA_RATE = 0.0; // Los productos ya incluyen IVA
    private List<Productos> ListaProductos;
    private Productos ProdSeleccionado;
    private Productos cantidadProd;
    private Productos productos;
    // private ManagerClientes manaCliente; // Eliminado
    private Productos productoMenor;
    private Ventas ventas;
    private Long id_producto;
    private String mensaje;
    private String codigoBusqueda;
    private List<Productos> resultadosBusqueda;
    private List<ItemCarrito> itemsCarrito;
    private Productos productoHierroPendiente;
    private Clientes idC;
    private String nomClientes = " ";
    private Productos productoMasVendido;
    private Double cantidadMasVendido;
    private boolean mostrarCamposHierro = false;
    private boolean ventaPagada = true;
    private boolean ventaEntregada = true;
    private boolean esVentaPorUnidad = false;

    // variables para dashboard
    private Double totalInversion;
    private BarChartModel barModel;

    public Double getTotalInversion() {
        return totalInversion;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public boolean isVentaPagada() {
        return ventaPagada;
    }

    public void setVentaPagada(boolean ventaPagada) {
        this.ventaPagada = ventaPagada;
    }

    public boolean isVentaEntregada() {
        return ventaEntregada;
    }

    public void setVentaEntregada(boolean ventaEntregada) {
        this.ventaEntregada = ventaEntregada;
    }

    public boolean isMostrarCamposHierro() {
        return mostrarCamposHierro;
    }

    public void setMostrarCamposHierro(boolean mostrarCamposHierro) {
        this.mostrarCamposHierro = mostrarCamposHierro;
    }

    public Productos getProductoMasVendido() {
        return productoMasVendido;
    }

    public Double getCantidadMasVendido() {
        return cantidadMasVendido;
    }

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

    public Productos getProductoHierroPendiente() {
        return productoHierroPendiente;
    }

    public void setProductoHierroPendiente(Productos productoHierroPendiente) {
        this.productoHierroPendiente = productoHierroPendiente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isEsVentaPorUnidad() {
        return esVentaPorUnidad;
    }

    public void setEsVentaPorUnidad(boolean esVentaPorUnidad) {
        this.esVentaPorUnidad = esVentaPorUnidad;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    public List<Medida> getListaMedidas() {
        return medidaFCL.findAll();
    }

    public List<Productos> getListaProductos() {
        this.ListaProductos = ProductoFCL.findAll();
        // this.resultadosBusqueda=ProductoFCL.findAll();
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
        // Asegurar que las medidas comunes existan
        if (managerMedida != null) {
            managerMedida.getListaMedidas();
        }
        cargarEstadisticas();
    }

    public void cargarEstadisticas() {
        try {
            Object[] result = detalleventaFL.obtenerProductoMasVendido();
            if (result != null && result.length == 2) {
                this.productoMasVendido = (Productos) result[0];
                this.cantidadMasVendido = ((Number) result[1]).doubleValue();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas: " + e.getMessage());
        }
        
        calcularTotalInversion();
        crearModeloBarras();
    }

    private void calcularTotalInversion() {
        this.totalInversion = 0.0;
        try {
            List<Productos> todos = ProductoFCL.findAll();
            if (todos != null) {
                for (Productos p : todos) {
                    if (p.getStock() != null && p.getPrecio_compra() != null && p.getStock() > 0) {
                        this.totalInversion += (p.getStock() * p.getPrecio_compra());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al calcular total inversión: " + e.getMessage());
        }
    }

    private void crearModeloBarras() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Capital vs Ventas");

        List<Object> values = new ArrayList<>();
        Double compras = (this.totalInversion != null) ? this.totalInversion : 0.0;
        Double ventasTotales = getTotalVentas();
        
        values.add(compras);
        values.add(ventasTotales);
        barDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)"); // Color Rojo claro - Inversión
        bgColor.add("rgba(75, 192, 192, 0.2)");  // Color Turquesa - Ventas
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(75, 192, 192)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        List<String> labels = new ArrayList<>();
        labels.add("Inversión en Bodega");
        labels.add("Total Ventas");
        data.setLabels(labels);
        barModel.setData(data);

        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setMin(0);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Relación Compras vs Ventas");
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        options.setLegend(legend);

        barModel.setOptions(options);
    }

    public String guardarProducto() {
        try {
            if (productos.getMedida() != null && (productos.getId_producto() == null || productos.getId_producto() == 0)) {
                String nombreConMedida = productos.getNom_producto();
                String sufijoMedida = " (" + productos.getMedida().getNombre() + ")";
                if (nombreConMedida != null && !nombreConMedida.endsWith(sufijoMedida)) {
                    productos.setNom_producto(nombreConMedida + sufijoMedida);
                }
            }
            System.out.println("Entro a guardar producto");
            
            if (productos.getPrecio_unitario() == null) {
                productos.setPrecio_unitario(0.0);
            }
            if (productos.getUnidadxmedida() == null) {
                productos.setUnidadxmedida(1);
            }
            
            if (productos.getId_producto() != null && productos.getId_producto() > 0) {
                Productos prodDB = ProductoFCL.find(productos.getId_producto());
                if (prodDB != null) {
                    Double stockAnterior = prodDB.getStock();
                    Double cantidadAgregada = productos.getStock();
                    prodDB.setStock(stockAnterior + cantidadAgregada);
                    prodDB.setPrecio_compra(productos.getPrecio_compra());
                    prodDB.setPrecio(productos.getPrecio());
                    prodDB.setCategoria(productos.getCategoria());
                    prodDB.setDescripcion(productos.getDescripcion());
                    prodDB.setMedida(productos.getMedida());
                    prodDB.setPrecio_unitario(productos.getPrecio_unitario());
                    prodDB.setUnidadxmedida(productos.getUnidadxmedida());
                    
                    ProductoFCL.edit(prodDB);
                    this.mensaje = "Stock actualizado. " + cantidadAgregada + " unidades sumadas. Total en bodega: " + prodDB.getStock();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto sumado al inventario exitosamente."));
                }
            } else {
                ProductoFCL.create(productos);
                this.mensaje = "Producto '" + productos.getNom_producto() + "' guardado correctamente.";
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto nuevo guardado correctamente."));
            }
            
            productos = new Productos(); // limpiar formulario
            mostrarCamposHierro = false; // limpiar variable
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "RegistrarProductos?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar producto", e.getMessage()));
        }
        return null;
    }

    public List<String> sugerirNombresProductos(String query) {
        List<Productos> filtrados = ProductoFCL.BuscarPorNombreParcial(query);
        List<String> sugerencias = new ArrayList<>();
        for (Productos p : filtrados) {
            sugerencias.add(p.getNom_producto());
        }
        return sugerencias;
    }

    public void onProductoSelect(SelectEvent<String> event) {
        String nombre = event.getObject();
        List<Productos> existentes = ProductoFCL.NombreProducto(nombre); 
        if (existentes != null && !existentes.isEmpty()) {
            Productos existente = existentes.get(0); 
            Double stockAnterior = existente.getStock();
            this.productos = existente; 
            this.productos.setStock(0.0); // Resetea el stock para que el usuario ingrese la nueva cantidad a sumar
            verificarHierro();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto Existente Seleccionado", "Stock en bodega: " + stockAnterior + ". Ingrese la nueva cantidad a sumar al stock."));
        }
    }

    public void verificarHierro() {
        if (productos == null) {
            mostrarCamposHierro = false;
            return;
        }

        String nombre = productos.getNom_producto();
        Medida m = productos.getMedida();

        boolean tieneHierro = nombre != null && nombre.toLowerCase().contains("hierro");
        boolean esQuintal = m != null && m.getNombre() != null && m.getNombre().equalsIgnoreCase("Quintal");

        mostrarCamposHierro = tieneHierro && esQuintal;

        if (!mostrarCamposHierro) {
            // Se podrían limpiar los campos de la entidad si se desea
        }

        System.out.println("Verificando Hierro: nombre=" + nombre + ", medida=" + (m != null ? m.getNombre() : "null")
                + " -> " + mostrarCamposHierro);
    }

    public void verificarHierroVenta() {
        if (id_producto == null || id_producto == 0) {
            mostrarCamposHierro = false;
            esVentaPorUnidad = false;
            return;
        }
        Productos p = ProductoFCL.find(id_producto);
        if (p != null) {
            String nombre = p.getNom_producto();
            Medida m = p.getMedida();
            boolean tieneHierro = nombre != null && nombre.toLowerCase().contains("hierro");
            boolean esQuintal = m != null && m.getNombre() != null && m.getNombre().equalsIgnoreCase("Quintal");
            mostrarCamposHierro = tieneHierro && esQuintal;
        } else {
            mostrarCamposHierro = false;
        }
        if (!mostrarCamposHierro) {
            esVentaPorUnidad = false;
        }
    }

    public Double getPrecioConfig(Long id) {
        if (id == null || id == 0)
            return 0.0;
        Productos p = ProductoFCL.find(id);
        return (p != null && p.getPrecio() != null) ? p.getPrecio() : 0.0;
    }

    public Double getPrecioUnitarioConfig(Long id) {
        if (id == null || id == 0)
            return 0.0;
        Productos p = ProductoFCL.find(id);
        return (p != null && p.getPrecio_unitario() != null) ? p.getPrecio_unitario() : 0.0;
    }

    public void procesarAgregarCarrito() {
        if (id_producto == null || id_producto == 0) {
            addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Seleccione un producto primero.");
            return;
        }
        Productos p = ProductoFCL.find(id_producto);
        if (p != null) {
            procesarAgregarCarrito(p, esVentaPorUnidad);
        }
    }

    public void eliminarProducto() {
        try {
            System.out.println("el producto a borrar es " + productos.getNom_producto());
            ProductoFCL.remove(productos);
            this.ListaProductos = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto eliminado."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el producto."));
        }

    }

    public void prepararEdicion(Long id) {
        ProdSeleccionado = new Productos();
        this.ProdSeleccionado = ProductoFCL.find(id);
        System.out.println("el cliente a editar es " + ProdSeleccionado.getNom_producto());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("cliente seleccionado es ".concat(ProdSeleccionado.getNom_producto())));

    }

    public void editarProducto() {
        try {
            ProductoFCL.edit(this.productos);
            this.ListaProductos = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto modificado correctamente."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el producto."));
        }
        codigoBusqueda = "";
        resultadosBusqueda = ProductoFCL.findAll();

    }

    public void buscarProducto() {

        this.resultadosBusqueda = new ArrayList<>();
        // this.resultadosBusqueda = new ArrayList<>();
        if (codigoBusqueda == null || codigoBusqueda.trim().length() < 3) {
            // Limpia la lista si el texto es muy corto o vacío
            this.resultadosBusqueda = ProductoFCL.findAll();
            return;
        }

        try {
            List<Productos> productosEntidad = ProductoFCL.BuscarPorNombreParcial(codigoBusqueda.trim());
            // --- Mapeo de Entidad JPA (Productos) a POJO Frontend (Producto) ---
            // this.resultadosBusqueda = new ArrayList<>();
            System.out.println("codigo busqueda " + codigoBusqueda);
            for (Productos entidad : productosEntidad) {
                System.out.println("entro al for " + entidad);
                this.resultadosBusqueda.add(mapEntidadToPojo(entidad));
            }
            // --- Fin Mapeo ---

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "No se pudo buscar el productio." + e.getMessage()));
            System.out.println("error exception " + e.getMessage());

        }
    }

    private Productos mapEntidadToPojo(Productos entidad) {
        // Asume que Productos tiene métodos getID, getNom_producto, etc.
        Productos pojo = new Productos();
        pojo.setId_producto(entidad.getId_producto()); // Usando los nombres de tu tabla (image_4f4883.png)
        pojo.setNom_producto(entidad.getNom_producto()); // 'nombre' de la columna (image_4f4883.png)
        pojo.setPrecio(entidad.getPrecio()); // 'precio' de la columna (image_4f4883.png)
        pojo.setStock(entidad.getStock());
        pojo.setMedida(entidad.getMedida());
        pojo.setPrecio_unitario(entidad.getPrecio_unitario());
        pojo.setUnidadxmedida(entidad.getUnidadxmedida());
        pojo.setPrecio_compra(entidad.getPrecio_compra());
        return pojo;
    }

    // --- Método Auxiliar para Mensajes de JSF ---
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    public void agregarAlCarrito(Productos productoAAgregar) {
        if (productoAAgregar.getStock() != null && productoAAgregar.getStock() <= 0) {
            addMessage(FacesMessage.SEVERITY_WARN, "Sin Stock", "El producto '" + productoAAgregar.getNom_producto() + "' no tiene existencias.");
            return;
        }

        // Lógica especial para Hierro
        boolean tieneHierro = productoAAgregar.getNom_producto() != null
                && productoAAgregar.getNom_producto().toLowerCase().contains("hierro");
        boolean esQuintal = productoAAgregar.getMedida() != null && productoAAgregar.getMedida().getNombre() != null
                && productoAAgregar.getMedida().getNombre().equalsIgnoreCase("Quintal");

        if (tieneHierro && esQuintal) {
            this.productoHierroPendiente = productoAAgregar;
            PrimeFaces.current().executeScript("PF('dlgSeleccionVentaHierro').show();");
            return;
        }

        procesarAgregarCarrito(productoAAgregar, false);
    }

    public void agregarAlPresupuesto(Productos productoAAgregar) {
        // Lógica especial para Hierro
        boolean tieneHierro = productoAAgregar.getNom_producto() != null
                && productoAAgregar.getNom_producto().toLowerCase().contains("hierro");
        boolean esQuintal = productoAAgregar.getMedida() != null && productoAAgregar.getMedida().getNombre() != null
                && productoAAgregar.getMedida().getNombre().equalsIgnoreCase("Quintal");

        if (tieneHierro && esQuintal) {
            this.productoHierroPendiente = productoAAgregar;
            PrimeFaces.current().executeScript("PF('dlgSeleccionVentaHierro').show();");
            return;
        }

        procesarAgregarCarrito(productoAAgregar, false);
    }

    public void confirmarVentaHierro(boolean esUnidad) {
        if (productoHierroPendiente != null) {
            procesarAgregarCarrito(productoHierroPendiente, esUnidad);
            productoHierroPendiente = null;
        }
    }

    private void procesarAgregarCarrito(Productos productoAAgregar, boolean esUnidad) {
        Optional<ItemCarrito> itemExistente = itemsCarrito.stream()
                .filter(item -> item.getProducto().getId_producto().equals(productoAAgregar.getId_producto())
                        && item.isEsVentaPorUnidad() == esUnidad)
                .findFirst();

        if (itemExistente.isPresent()) {
            // Si ya está en el carrito con el mismo tipo de venta, solo incrementa la
            // cantidad
            itemExistente.get().setCantidad(itemExistente.get().getCantidad() + 1.0);
            System.out.println(productoAAgregar.getNom_producto() + " (Cantidad incrementada).");
        } else {
            // Si es nuevo o distinto tipo de venta, agrégalo
            ItemCarrito nuevoItem = new ItemCarrito(productoAAgregar);
            nuevoItem.setEsVentaPorUnidad(esUnidad);
            itemsCarrito.add(nuevoItem);
            System.out.println(productoAAgregar.getNom_producto() + (esUnidad ? " (Unidad)" : " (Quintal)")
                    + " agregado al carrito.");
        }

        System.out.println("Producto añadido. Total de items en carrito: " + this.itemsCarrito.size());
    }

    public void eliminarItem(ItemCarrito item) {
        itemsCarrito.remove(item);

        addMessage(FacesMessage.SEVERITY_INFO, "Info", "Producto eliminado del carrito.");
        System.out.println("producto borrado ");
    }

    public void eliminarDelCarrito(ItemCarrito item) {
        eliminarItem(item);
    }

    public void validarPrecio(ItemCarrito item) {
        Double precioVenta = item.getPrecioAplicado();
        Double precioCompra = item.getProducto().getPrecio_compra();
        
        Double costoReal = precioCompra != null ? precioCompra : 0.0;
        if (item.isEsVentaPorUnidad() && item.getProducto().getUnidadxmedida() != null && item.getProducto().getUnidadxmedida() > 0) {
            costoReal = costoReal / item.getProducto().getUnidadxmedida();
        }

        if (precioVenta < costoReal) {
            item.setPrecioAplicado(costoReal);
            addMessage(FacesMessage.SEVERITY_WARN, "Precio Inválido", "El precio no puede ser menor al costo ($" + String.format("%.2f", costoReal) + "). Ajustado automáticamente.");
        }
    }

    public void finalizarVenta() {
        if (itemsCarrito.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "El carrito está vacío. Agregue productos.");
            return;
        }

        // 1. Verificar stock (IMPORTANTE: Lógica de negocio)
        System.out.println("id cliente #" + idC);
        // id_cliente = Math.toIntExact(idC);

        verificarStock(false);

        // 3. Llamar al Facade/Microservicio para persistir y decrementar stock.
        // --- SIMULACIÓN ---
        System.out.println("Venta finalizada. Total: " + getTotalVenta());
        addMessage(FacesMessage.SEVERITY_INFO, "Éxito",
                "¡Venta registrada exitosamente! Total: $" + String.format("%.2f", getTotalVenta()));

        this.itemsCarrito = new ArrayList<>();
        this.resultadosBusqueda = ProductoFCL.listarPrimeros10();
        this.itemsCarrito.clear();
        PrimeFaces.current().executeScript("PF('dlgVentaExitosa').show();");
        // --- FIN SIMULACIÓN ---
        nomClientes = "";
        ventaPagada = true;
        ventaEntregada = true;
        cargarEstadisticas();
    }

    public void finalizarPresupuesto() {
        if (itemsCarrito.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "El carrito está vacío.");
            return;
        }

        // Forzar estados para presupuesto
        this.ventaPagada = false;
        this.ventaEntregada = false;

        // Reutilizamos la lógica de verificarStock que se encarga de crear la Venta y
        // sus Detalles
        // Nota: En un presupuesto real a veces no se descuenta stock, pero el usuario
        // pidió "guardar como factura no pagada no entregada"
        // lo que implica usar la misma lógica de venta pendiente.
        verificarStock(true);

        addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Presupuesto guardado como venta pendiente.");

        this.itemsCarrito = new ArrayList<>();
        this.resultadosBusqueda = ProductoFCL.listarPrimeros10();
        PrimeFaces.current().executeScript("PF('dlgVentaExitosa').show();");

        nomClientes = "";
        ventaPagada = true; // reset para próxima venta normal
        ventaEntregada = true;
    }

    public void verificarStock(boolean esPresupuesto) {
        cantidadProd = new Productos();
        for (int i = 0; i < itemsCarrito.size(); i++) {
            ItemCarrito item = itemsCarrito.get(i);
            Long id_item;
            System.out.println("Producto: " + item.getProducto().getNom_producto());
            System.out.println("Id: " + item.getProducto().getId_producto());
            id_item = item.getProducto().getId_producto();
            System.out.println("Cantidad: " + item.getCantidad());
            System.out.println("Precio unitario: $" + item.getSubtotal());
            System.out.println("Subtotal: $" + item.getSubtotal());
            System.out.println("---------------------------");
            cantidadProd = ProductoFCL.find(id_item);
            LocalDateTime fechaVenta = LocalDateTime.now();
            Timestamp fecha = Timestamp.valueOf(fechaVenta);
            double cantidadADescontar = item.getCantidad();
            if (item.isEsVentaPorUnidad() && item.getProducto().getUnidadxmedida() != null
                    && item.getProducto().getUnidadxmedida() > 0) {
                cantidadADescontar = item.getCantidad() / item.getProducto().getUnidadxmedida();
                System.out.println("Conversión Hierro: " + item.getCantidad() + " unidades -> " + cantidadADescontar
                        + " quintales");
            }

            if (esPresupuesto || cantidadADescontar <= cantidadProd.getStock()) {
                ProductoFCL.actualizarStock(id_item, cantidadADescontar);
            } else {
                System.out.println("producto agotado");
                PrimeFaces.current().executeScript("PF('dlgAgotado'.show())");
                return;
            }
            if (i == 0) {
                ventas = new Ventas();
                ventas.setId_cliente(idC);
                ventas.setFecha_venta(new Date());
                System.out.println("fecha " + fecha);
                ventas.setTotal(getTotalVenta());
                ventas.setPagado(ventaPagada);
                ventas.setEntregado(ventaEntregada);
                System.out.println(
                        "cantidad inventario" + item.getCantidad() + " y cantidad stock" + cantidadProd.getStock());
                System.out.println("ventas " + ventas.getFecha_venta());
                System.out.println("ventas " + ventas.getTotal() + " y " + ventas.getFecha_venta());
                System.out.println("ventas " + ventas.getId_cliente());
                ventasFCD.create(ventas);

            }

            // 2. Crear objeto Venta (venta y Detalle_venta)
            System.out.println("ventas" + ventas.getTotal());
            // ventasFCD.create(ventas);
            detalleventa = new DetalleVenta();
            System.out.println("Item #" + (i + 1));
            System.out.println("Producto ID: " + item.getProducto().getId_producto());
            System.out.println("Cantidad: " + item.getCantidad());
            System.out.println("Subtotal: $" + item.getSubtotal());
            detalleventa.setVenta(ventas);
            detalleventa.setCantidad(item.getCantidad());
            detalleventa.setProducto(item.getProducto());
            // detalleventa.setId_producto(Math.toIntExact(item.getProducto().getId_producto()));

            double precioAplicado = item.getPrecioAplicado();
            detalleventa.setPrecio_unitario(precioAplicado);

            double subtotalReal = item.getCantidad() * precioAplicado;
            // Redondear a 2 decimales antes de setearlo
            subtotalReal = Math.round(subtotalReal * 100.0) / 100.0;
            System.out.println("Subtotal real : $" + subtotalReal);
            detalleventa.setSubtotal(subtotalReal);
            detalleventa.setPagado(ventaPagada);
            detalleventa.setEntregado(ventaEntregada);
            try {
                detalleventaFL.create(detalleventa);
                System.out.println("Detalle guardado con éxito para producto: " + item.getProducto().getNom_producto());
            } catch (Exception e) {
                System.err.println("CRÍTICO: Error al persistir detalle_venta: " + e.getMessage());
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error crítico",
                                "No se pudo guardar el detalle del producto: " + item.getProducto().getNom_producto()));
            }

        }

    }

    public void seleccionarClienteYFinalizar(Clientes c) {
        System.out.println("Asignando cliente para finalizar: " + c.getNombres());
        this.idC = c;
        this.nomClientes = c.getNombres();
        finalizarVenta();
    }

    public void idCliente(Clientes c) {
        System.out.println("cliente seleccionado: " + c.getNombres());
        idC = c;
        nomClientes = c.getNombres();
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

    public Double getTotalGanancias() {
        Double totalGanancia = 0.0;
        try {
            List<DetalleVenta> detalles = detalleventaFL.obtenerTodosConProductos();
            if (detalles != null) {
                for (DetalleVenta d : detalles) {
                    Productos p = d.getProducto();
                    Double pCompraBulk = (p.getPrecio_compra() != null) ? p.getPrecio_compra() : 0.0;
                    Double costEfectivo = pCompraBulk;

                    // Si el producto tiene factor de conversión (ej. Hierro)
                    if (p.getUnidadxmedida() != null && p.getUnidadxmedida() > 0) {
                        // Heurística: ¿Se vendió por UNIDAD?
                        // Comparamos el precio de esta venta con el precio unitario configurado
                        Double pVentaUnidad = (p.getPrecio_unitario() != null) ? p.getPrecio_unitario() : 0.0;

                        // Si el precio de venta es igual al precio unitario, calculamos costo por
                        // unidad
                        if (Math.abs(d.getPrecio_unitario() - pVentaUnidad) < 0.01) {
                            costEfectivo = pCompraBulk / p.getUnidadxmedida();
                        }
                    }

                    totalGanancia += (d.getPrecio_unitario() - costEfectivo) * d.getCantidad();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al calcular ganancias: " + e.getMessage());
        }
        return Math.round(totalGanancia * 100.0) / 100.0;
    }

    public void buscarProdNombre() {

        this.resultadosBusqueda = new ArrayList<>();
        // this.resultadosBusqueda = new ArrayList<>();
        if (codigoBusqueda == null || codigoBusqueda.trim().length() < 3) {
            // Limpia la lista si el texto es muy corto o vacío
            this.resultadosBusqueda = ProductoFCL.listarPrimeros10();
            return;
        }

        try {
            List<Productos> pTemp = ProductoFCL.BuscarPorNombreParcial(codigoBusqueda.trim());
            this.resultadosBusqueda = new ArrayList<>();
            for (int i = 0; i < pTemp.size() && i < 10; i++) {
                this.resultadosBusqueda.add(pTemp.get(i));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "No se pudo buscar el productio." + e.getMessage()));
            System.out.println("error exception " + e.getMessage());

        }
    }

    public void agregarPrimerResultado() {
        if (resultadosBusqueda != null && !resultadosBusqueda.isEmpty()) {
            Productos p = resultadosBusqueda.get(0);
            if (p.getStock() != null && p.getStock() > 0) {
                agregarAlCarrito(p);
            } else {
                addMessage(FacesMessage.SEVERITY_WARN, "Sin Stock", "El primer resultado no tiene existencias.");
            }
        }
    }

    public void preProcessPDF(Object document) {
        com.lowagie.text.Document pdf = (com.lowagie.text.Document) document;
        pdf.open();

        try {
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);

            Paragraph title = new Paragraph("Materiales de construcción y transporte MARTINEZ AGUILAR", fontTitulo);
            title.setAlignment(Element.ALIGN_CENTER);
            pdf.add(title);

            Paragraph contact = new Paragraph("Teléfono: 7209 8154 | Email: luismartinezaguilar49@gmail.com", fontSub);
            contact.setAlignment(Element.ALIGN_CENTER);
            pdf.add(contact);

            Paragraph address = new Paragraph("Dirección: Canton el paraiso turin ahuachapan", fontSub);
            address.setAlignment(Element.ALIGN_CENTER);
            address.setSpacingAfter(20);
            pdf.add(address);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void postProcessPDF(Object document) {
        // Opcional: Agregar pie de página o cerrar recursos si fuera necesario
    }

    public Clientes getClienteSeleccionado() {
        return idC;
    }

    public void setClienteSeleccionado(Clientes clienteSeleccionado) {
        this.idC = clienteSeleccionado;
    }

    public Long getId_producto() {
        return id_producto;
    }

    public void setId_producto(Long id_producto) {
        this.id_producto = id_producto;
    }

    public String limpiarCarrito() {
        System.out.println("DEBUG: Entrando a limpiarCarrito()");
        try {
            this.itemsCarrito = new ArrayList<>();
            this.resultadosBusqueda = ProductoFCL.listarPrimeros10();
            nomClientes = "";
            System.out.println("DEBUG: Carrito limpio, redireccionando a DetalleFacturas");
            return "DetalleFacturas?faces-redirect=true";
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Error en limpiarCarrito: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
