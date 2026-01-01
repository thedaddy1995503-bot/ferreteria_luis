/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.util.List;

/**
 *
 * @author juank
 */

@SessionScoped
@Named("ClienteController")
public class ClienteController implements Serializable{
    
    @EJB
    private ClientesFacadeLocal clienteFCL;
    private Clientes cliente;
    private String codigoBusqueda;
    private List<Clientes> resultadosBusqueda;
    
    
    
     public void buscarCliente() {

        this.resultadosBusqueda = new ArrayList<>();
        //this.resultadosBusqueda = new ArrayList<>();
        if (codigoBusqueda == null || codigoBusqueda.trim().length() < 3) {
            // Limpia la lista si el texto es muy corto o vacío
            this.resultadosBusqueda = clienteFCL.listarPrimeros10();
            return;
        }

        try {
            List<Clientes> productosEntidad = clienteFCL.BuscarNombre(codigoBusqueda.trim());
            // --- Mapeo de Entidad JPA (Productos) a POJO Frontend (Producto) ---
            //this.resultadosBusqueda = new ArrayList<>();
            System.out.println("codigo busqueda " + codigoBusqueda);
            for (Clientes entidad : productosEntidad) {
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
     
     private Clientes mapEntidadToPojo(Clientes entidad) {
        // Asume que Productos tiene métodos getID, getNom_producto, etc.
        Clientes pojo = new Clientes();
        pojo.setId_Cliente(entidad.getId_Cliente()); // Usando los nombres de tu tabla (image_4f4883.png)
        pojo.setNombre(entidad.getNombre());  // 'nombre' de la columna (image_4f4883.png)
        pojo.setApellidos(entidad.getApellidos());  // 'precio' de la columna (image_4f4883.png)
        //pojo.setStock(entidad.getStock());
        return pojo;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public String getCodigoBusqueda() {
        return codigoBusqueda;
    }

    public void setCodigoBusqueda(String codigoBusqueda) {
        this.codigoBusqueda = codigoBusqueda;
    }

    public List<Clientes> getResultadosBusqueda() {
        return resultadosBusqueda;
    }

    public void setResultadosBusqueda(List<Clientes> resultadosBusqueda) {
        this.resultadosBusqueda = resultadosBusqueda;
    }
    
     @PostConstruct
    public void init() {
        cliente = new Clientes();
        //this.mensaje = "";
        this.resultadosBusqueda = clienteFCL.listarPrimeros10();
        //this.productoMenor=ProductoFCL.obtenerProductoConMenorInventario();

    }
}
