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
      // NOTA: Asume que los atributos de tu clase Usuario son 'nombreUsuario' y 'clave'.
        try {
            // 1. Usa JPQL (SELECT u FROM Entidad)
            // 2. Usa el alias correcto (u.nombreUsuario)
            // 3. Retorna un solo resultado
            return  em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombre_usuario = :nomUsuario AND u.clave = :contra", Usuario.class)
                    // Asigna los parámetros
                    .setParameter("nomUsuario", nombre)
                    .setParameter("contra", contra)
                    // Retorna un único objeto Usuario o lanza NoResultException si no encuentra nada
                    .getSingleResult();

        } catch (jakarta.persistence.NoResultException e) {
            // Si no se encuentra el usuario, retorna null (ideal para un login)
            return null;
        } catch (Exception e) {
            // Manejo de errores de la BD
            e.printStackTrace();
            return null;
        }
    }

    

    public UsuarioFacade() {
        super(Usuario.class);
    }

}
