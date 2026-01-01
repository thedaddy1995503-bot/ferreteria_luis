/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EJB.Productos.Ventas;

import Entity.Ventas;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author juank
 */
@Stateless
public class VentasFacade extends AbstractFacade<Ventas> implements VentasFacadeLocal {

    @PersistenceContext(unitName = "miPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VentasFacade() {
        super(Ventas.class);
    }
    @Override
    public int obtenerUltimoIdVenta() {
        try {
            return em.createQuery(
                "SELECT v.id_venta FROM ventas v ORDER BY v.id_venta DESC",
                Ventas.class)
                .setMaxResults(1)
                .getFirstResult();
        } catch (Exception e) {
            return 0; // si no hay registros
        }
    }
    
    @Override
    public List<Ventas> obtenerVentasConCliente() {
    return em.createQuery("SELECT v FROM Ventas v JOIN FETCH v.id_cliente", Ventas.class)
             .getResultList();
}
    
    @Override
    public Double obtenerTotalVentas() {
        try {
            // Usamos SUM sobre el campo total de la entidad Ventas
            Double total = em.createQuery(
                "SELECT SUM(v.total) FROM Ventas v", Double.class)
                .getSingleResult();

            return (total != null) ? total : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
}
