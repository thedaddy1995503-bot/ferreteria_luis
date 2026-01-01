/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EJB.DetalleVentas;

import Entity.DetalleVenta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author juank
 */
@Stateless
public class DetalleVentaFacade extends AbstractFacade<DetalleVenta> implements DetalleVentaFacadeLocal {

    @PersistenceContext(unitName = "miPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleVentaFacade() {
        super(DetalleVenta.class);
    }
    
    @Override
    public List<DetalleVenta> buscarPorVenta(Long idVenta) {
        TypedQuery<DetalleVenta> query = em.createQuery(
            "SELECT d FROM DetalleVenta d WHERE d.venta.id_venta = :idVenta", 
            DetalleVenta.class);
        query.setParameter("idVenta", idVenta);
        return query.getResultList();
    }
    
}
