/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EJB;

import Entity.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author juank
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "miPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

   
    @Override
    public Usuario login(String nombre, String contra) {
        System.out.println("[UsuarioFacade] Iniciando intento de login para el usuario: " + nombre);
        try {
            Usuario usuario = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombre_usuario = :nomUsuario AND u.clave = :contra", Usuario.class)
                    .setParameter("nomUsuario", nombre)
                    .setParameter("contra", contra)
                    .getSingleResult();
            
            System.out.println("[UsuarioFacade] CONEXION EXITOSA. Usuario encontrado: " + usuario.getNombre_usuario() + " (Nombre: " + usuario.getNombre_completo() + ", Rol: " + usuario.getRol() + ")");
            return usuario;
        } catch (jakarta.persistence.NoResultException e) {
            System.out.println("[UsuarioFacade] BD respondio correctamente: No se encontro ningun usuario con esas credenciales.");
            return null;
        } catch (Exception e) {
            System.err.println("[UsuarioFacade] ERROR DE CONEXION O BD (Railway): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al conectar o consultar la base de datos en Railway: " + e.getMessage(), e);
        }
    }

    

    public UsuarioFacade() {
        super(Usuario.class);
    }

}
