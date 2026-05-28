/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this tCemplate
 */
package Controller;

import Clientes.ClientesFacadeLocal;
import Entity.Clientes;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author juank
 */
@SessionScoped
@Named("ManagerClientes")
public class ManagerClientes implements Serializable {

    @jakarta.inject.Inject
    private ClienteController clienteController;

    @jakarta.inject.Inject
    private ManagerProductos managerProductos;

    @EJB
    private ClientesFacadeLocal clienteFCL;
    private List<Clientes> ListaClientes;
    private Clientes clientes;
    private Clientes clienteSeleccionado;
    private Clientes facturaCliente;
    private String criterioBusqueda;
    private List<Clientes> resultadosBusqueda;
    private String mensaje;
    private Date fechaRegistro;
    private Clientes ClienteSeleccionado;
    private String codigoBusqueda;

    public String getCodigoBusqueda() {
        return codigoBusqueda;
    }

    public void setCodigoBusqueda(String codigoBusqueda) {
        this.codigoBusqueda = codigoBusqueda;
    }

    public Clientes getFacturaCliente() {
        return facturaCliente;
    }

    public void setFacturaCliente(Clientes facturaCliente) {
        this.facturaCliente = facturaCliente;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Clientes> getListaClientes() {
        this.ListaClientes = clienteFCL.findAll();
        return ListaClientes;
    }

    public void setListaClientes(List<Clientes> ListaClientes) {
        this.ListaClientes = ListaClientes;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public Clientes getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Clientes clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public List<Clientes> getResultadosBusqueda() {
        this.ListaClientes = clienteFCL.listarPrimeros10();
        return ListaClientes;
    }

    public void setResultadosBusqueda(List<Clientes> resultadosBusqueda) {
        this.resultadosBusqueda = resultadosBusqueda;
    }

    public void guardarClientes() {

        try {
            clientes.setFecha_registro(new Date());
            System.out.println("Entro clientes " + clientes.getFecha_registro());
            clienteSeleccionado = null;
            String dui = clientes.getDui();

            clienteSeleccionado = clienteFCL.BuscarDui(dui);
            if (clienteSeleccionado != null) {

                System.out.println("Cliente ya existente " + clienteSeleccionado.getNombres());
                this.mensaje = "clientes '" + clientes.getNombres() + "' ya existente.";
            } else {
                clienteFCL.create(clientes);
                System.out.println("Cliente creado correctamente");
                this.mensaje = "clientes creado correctamente";
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "cliente guardado correctamente", null));

                // Auto-seleccionar en Ventas si managerProductos está inyectado
                if (managerProductos != null) {
                    managerProductos.setClienteSeleccionado(clientes);
                    managerProductos.setNomClientes(clientes.getNombres());
                }

                this.clientes = new Clientes();
                if (clienteController != null) {
                    clienteController.limpiarBusqueda();
                }
            }

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar cliente", ex.getMessage()));
            System.out.println("Error creando cliente" + ex.getMessage());
        }

    }

    public void buscarCliente() {

        this.resultadosBusqueda = new ArrayList<>();
        // this.resultadosBusqueda = new ArrayList<>();
        if (codigoBusqueda == null || codigoBusqueda.trim().length() < 3) {
            // Limpia la lista si el texto es muy corto o vacío
            this.resultadosBusqueda = clienteFCL.listarPrimeros10();
            return;
        }

        try {
            List<Clientes> productosEntidad = clienteFCL.BuscarNombre(codigoBusqueda.trim());
            // --- Mapeo de Entidad JPA (Productos) a POJO Frontend (Producto) ---
            // this.resultadosBusqueda = new ArrayList<>();
            System.out.println("codigo busqueda " + codigoBusqueda);
            for (Clientes entidad : productosEntidad) {
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

    public void prepararEdicion(Long id) {
        ClienteSeleccionado = new Clientes();
        this.ClienteSeleccionado = clienteFCL.find(id);
        System.out.println("el cliente a editar es " + ClienteSeleccionado.getNombres());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("cliente seleccionado es ".concat(ClienteSeleccionado.getNombres())));

    }

    public void editarClientes() {
        try {
            clienteFCL.edit(clienteSeleccionado);
            this.ListaClientes = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cliente modificado correctamente."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo Actualizar el cliente."));
        }

    }

    public void eliminarCliente(Object id) {

        try {
            clientes = clienteFCL.find(id);
            System.out.println("el cliente a borrar es " + clientes.getNombres());
            clienteFCL.remove(clientes);
            this.ListaClientes = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cliente eliminado."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el cliente."));
        }

    }

    private Clientes mapEntidadToPojo(Clientes entidad) {
        // Asume que Productos tiene métodos getID, getNom_producto, etc.
        Clientes pojo = new Clientes();
        pojo.setId_cliente(entidad.getId_cliente()); // Usando los nombres de tu tabla (image_4f4883.png)
        pojo.setNombres(entidad.getNombres()); // 'nombre' de la columna (image_4f4883.png)
        pojo.setApellidos(entidad.getApellidos()); // 'precio' de la columna (image_4f4883.png)
        // pojo.setStock(entidad.getStock());
        return pojo;
    }

    public void clienteSeleccionadoF(Clientes c) {
        facturaCliente = new Clientes();
        facturaCliente = c;
        System.out.println("cliente seleccionado " + facturaCliente.getNombres());

    }

    public long idClienteSeleccionado() {
        return facturaCliente.getId_cliente();
    }

    public void buscarProducto() {

        this.resultadosBusqueda = new ArrayList<>();
        // this.resultadosBusqueda = new ArrayList<>();
        if (codigoBusqueda == null || codigoBusqueda.trim().length() < 3) {
            // Limpia la lista si el texto es muy corto o vacío
            this.resultadosBusqueda = clienteFCL.listarPrimeros10();
            return;
        }

        try {
            List<Clientes> productosEntidad = clienteFCL.BuscarNombre(codigoBusqueda.trim());
            // --- Mapeo de Entidad JPA (Productos) a POJO Frontend (Producto) ---
            // this.resultadosBusqueda = new ArrayList<>();
            System.out.println("codigo busqueda " + codigoBusqueda);
            for (Clientes entidad : productosEntidad) {
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

    public void prepararNuevoCliente() {
        this.clientes = new Clientes();
        this.clientes.setFecha_registro(new Date());
    }

    public List<Clientes> completarClientes(String query) {
        List<Clientes> todos = getListaClientes();
        List<Clientes> filtrados = new ArrayList<>();

        for (Clientes c : todos) {
            if (c.getNombres().toLowerCase().contains(query.toLowerCase()) ||
                    c.getDui().contains(query)) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    @PostConstruct
    public void init() {
        clientes = new Clientes();
        this.mensaje = "";
        this.resultadosBusqueda = clienteFCL.listarPrimeros10();

    }

}
