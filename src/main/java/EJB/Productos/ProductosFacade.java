/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EJB.Productos;

import Entity.Productos;
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
public class ProductosFacade extends AbstractFacade<Productos> implements ProductosFacadeLocal {

    @PersistenceContext(unitName = "miPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductosFacade() {
        super(Productos.class);
    }
    @Override
    public Productos BuscarNombre(String Nombre) {
        try {

            return em.createQuery(
                    "SELECT p FROM Productos p WHERE p.nom_producto LIKE :nomProd", Productos.class)
                    // Asigna los parámetros
                    .setParameter("nomProd",  "%" + Nombre + "%")
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
    public List<Productos> listarPrimeros10() {
        return em.createQuery("SELECT p FROM Productos p ORDER BY p.Id_Producto ASC", Productos.class)
             .setMaxResults(5)
             .getResultList();
    
}
    
     @Override
    public Productos obtenerProductoConMenorInventario() {
        try {
            return em.createQuery(
                "SELECT p FROM Productos p ORDER BY p.stock ASC",
                Productos.class)
                .setMaxResults(1)
                .getSingleResult();
        } catch (Exception e) {
            return null; // si no hay productos
        }
    }
    
    // Modifica el método para que devuelva una LISTA y refleje la búsqueda parcial
    @Override
    public List<Productos> BuscarPorNombreParcial(String nombre) {
    try {
        // La consulta debe buscar coincidencias parciales con LIKE y el comodín '%'
        // Usa el comodín '%' al asignar el parámetro
        return em.createQuery(
            "SELECT p FROM Productos p WHERE p.nom_producto LIKE :nomProd", Productos.class)
            .setParameter("nomProd", "%" + nombre + "%") // CLAVE: Añadir los comodines
            .getResultList(); // CLAVE: Usar getResultList() para devolver múltiples resultados
    } catch (Exception e) {
        // En caso de error (ej. SQL, etc.), imprime y devuelve una lista vacía
        e.printStackTrace();
        return new ArrayList<>(); 
    }
}
    
        // Método para actualizar stock después de una venta
    public void actualizarStock(Long idProducto, int cantidadVendida) {
        Productos producto = em.find(Productos.class, idProducto);
        if (producto != null) {
            Long stockActual = producto.getStock();

            if (stockActual >= cantidadVendida) {
                producto.setStock(stockActual - cantidadVendida);
                em.merge(producto); // actualiza en BD
            } else {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNom_producto());
            }
        } else {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + idProducto);
        }
    }

}
