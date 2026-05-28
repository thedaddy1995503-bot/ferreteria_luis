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
import java.util.Date;
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
                "SELECT d FROM DetalleVenta d JOIN FETCH d.producto JOIN FETCH d.venta WHERE d.venta.id_venta = :idVenta",
                DetalleVenta.class);
        query.setParameter("idVenta", idVenta);
        return query.getResultList();
    }

    @Override
    public List<DetalleVenta> obtenerTodosConProductos() {
        TypedQuery<DetalleVenta> query = em.createQuery(
                "SELECT d FROM DetalleVenta d JOIN FETCH d.producto JOIN FETCH d.venta",
                DetalleVenta.class);
        return query.getResultList();
    }

    @Override
    public Object[] obtenerProductoMasVendido() {
        try {
            return (Object[]) em.createQuery(
                    "SELECT d.producto, SUM(d.cantidad) as total FROM DetalleVenta d GROUP BY d.producto ORDER BY total DESC")
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<DetalleVenta> obtenerPorRangoFechas(Date inicio, Date fin) {
        TypedQuery<DetalleVenta> query = em.createQuery(
                "SELECT d FROM DetalleVenta d JOIN FETCH d.producto JOIN FETCH d.venta WHERE d.venta.fecha_venta BETWEEN :inicio AND :fin",
                DetalleVenta.class);
        query.setParameter("inicio", inicio);
        query.setParameter("fin", fin);
        return query.getResultList();
    }
}
