package Controller;

import EJB.MedidaFacadeLocal;
import Entity.Medida;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Controlador para gestionar la entidad Medida desde la interfaz web.
 * 
 * @author juank
 */
@Named("ManagerMedida")
@SessionScoped
public class ManagerMedida implements Serializable {

    @EJB
    private MedidaFacadeLocal medidaFCL;

    private Medida medida;
    private List<Medida> listaMedidas;
    private Medida medidaSeleccionada;

    public ManagerMedida() {
    }

    @PostConstruct
    public void init() {
        medida = new Medida();
        verificarYAgregarMedidasComunes();
        eliminarMedidaIncorrecta();
    }

    private void eliminarMedidaIncorrecta() {
        List<Medida> existentes = medidaFCL.findAll();
        for (Medida m : existentes) {
            if (m.getNombre().equalsIgnoreCase("quintal 1")) {
                medidaFCL.remove(m);
            }
        }
    }

    private void verificarYAgregarMedidasComunes() {
        String[] comunes = { "Unidad", "Kilogramo (Kg)", "Libra (Lb)", "Metro", "Pulgada", "Litro", "Galón", "Quintal",
                "Bolsa" };
        List<Medida> existentes = medidaFCL.findAll();
        for (String nombre : comunes) {
            boolean existe = existentes.stream().anyMatch(m -> m.getNombre().equalsIgnoreCase(nombre));
            if (!existe) {
                medidaFCL.create(new Medida(nombre));
            }
        }
    }

    public void guardarMedida() {
        try {
            medidaFCL.create(medida);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Medida guardada correctamente."));
            medida = new Medida(); // Limpiar formulario
            listaMedidas = null; // Forzar recarga de la lista
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "No se pudo guardar la medida: " + e.getMessage()));
        }
    }

    public void eliminarMedida(Medida m) {
        try {
            medidaFCL.remove(m);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Medida eliminada correctamente."));
            listaMedidas = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "No se pudo eliminar la medida. Verifique que no esté siendo usada por algún producto."));
        }
    }

    public void prepararEdicion(Medida m) {
        this.medidaSeleccionada = m;
    }

    public void editarMedida() {
        try {
            medidaFCL.edit(medidaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Medida actualizada correctamente."));
            listaMedidas = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "No se pudo actualizar la medida: " + e.getMessage()));
        }
    }

    // Getters y Setters
    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public List<Medida> getListaMedidas() {
        if (listaMedidas == null) {
            listaMedidas = medidaFCL.findAll();
        }
        return listaMedidas;
    }

    public Medida getMedidaSeleccionada() {
        return medidaSeleccionada;
    }

    public void setMedidaSeleccionada(Medida medidaSeleccionada) {
        this.medidaSeleccionada = medidaSeleccionada;
    }
}
