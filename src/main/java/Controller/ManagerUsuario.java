/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import EJB.UsuarioFacadeLocal;
import Entity.Usuario;
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
@Named("ManagerUsuario")
public class ManagerUsuario implements Serializable{
    @EJB
    private UsuarioFacadeLocal UsuarioFCL;
    private List<Usuario> ListaUsuario;
    private Usuario UsuarioSeleccionado;
    
    private String nomUsuario;
    private String contras;

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getContras() {
        return contras;
    }

    public void setContras(String contras) {
        this.contras = contras;
    }
    
    private String mensajeError;

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
    
    public String login(){
        UsuarioSeleccionado=null;
        
        if (this.nomUsuario == null || this.nomUsuario.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario es obligatorio."));
            return null;
        }
        
        // SEGURIDAD: Evitar espacios en blanco en el usuario (medida anti-phishing / suplantación)
        if (this.nomUsuario.contains(" ")) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seguridad", "El usuario no puede contener espacios en blanco."));
            return null;
        }
        
        String u = this.nomUsuario.trim(); 
        String c = this.contras;
        String valor=null;
        System.out.println("Entro");
        UsuarioSeleccionado=UsuarioFCL.login(u, c);
        
        if (UsuarioSeleccionado!=null){
             valor= "dashboard?faces-redirect=true";
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario autenticado correctamente."));
             System.out.println("el cliente encontrado"+UsuarioSeleccionado.getNombre_completo());
        }else{
            valor= null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos."));
        }
        return valor;
    }

    // Método para cerrar sesión de forma segura
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }

    public List<Usuario> getListaUsuario() {
        this.ListaUsuario=UsuarioFCL.findAll();
        return ListaUsuario;
    }

    public void setListaUsuario(List<Usuario> ListaUsuario) {
        this.ListaUsuario = ListaUsuario;
    }

    public Usuario getUsuarioSeleccionado() {
        return UsuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario UsuarioSeleccionado) {
        this.UsuarioSeleccionado = UsuarioSeleccionado;
    }
    
    
    
    
}
