/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clientes;

import Entity.Clientes;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juank
 */
@Stateless
public class ClientesFacade extends AbstractFacade<Clientes> implements ClientesFacadeLocal {

    @PersistenceContext(unitName = "miPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesFacade() {
        super(Clientes.class);
    }
    
    
     @Override
    public Clientes BuscarDui(String dui) {
        try {

            return em.createQuery(
                    "SELECT p FROM Clientes p WHERE p.dui LIKE :duiCliente", Clientes.class)
                    // Asigna los parámetros
                    .setParameter("duiCliente",  "%" + dui + "%")
                    // Retorna un único objeto Usuario o lanza NoResultException si no encuentra nada
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            // Manejo de errores de la BD
            e.printStackTrace();
            return null;
        }

    }
     
    
    @Override
    public List<Clientes> BuscarNombre(String nombre) {
        try {

            return em.createQuery(
                    "SELECT p FROM Clientes p WHERE p.nombre LIKE :nomCliente", Clientes.class)
                    // Asigna los parámetros
                    .setParameter("nomCliente",  "%" + nombre + "%")
                    // Retorna un único objeto Usuario o lanza NoResultException si no encuentra nada
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    
       @Override
    public List<Clientes> listarPrimeros10() {
        return em.createQuery("SELECT c FROM Clientes c ORDER BY c.Id_Cliente ASC", Clientes.class)
             .setMaxResults(2)
             .getResultList();
    
}
}
