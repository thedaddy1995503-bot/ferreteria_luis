/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package EJB.DetalleVentas;

import Entity.DetalleVenta;
import jakarta.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 *
 * @author juank
 */
@Local
public interface DetalleVentaFacadeLocal {

    void create(DetalleVenta detalleVenta);

    void edit(DetalleVenta detalleVenta);

    void remove(DetalleVenta detalleVenta);

    DetalleVenta find(Object id);

    List<DetalleVenta> findAll();

    List<DetalleVenta> findRange(int[] range);

    List<DetalleVenta> buscarPorVenta(Long idVenta);

    List<DetalleVenta> obtenerTodosConProductos();

    int count();

    Object[] obtenerProductoMasVendido();

    List<DetalleVenta> obtenerPorRangoFechas(Date inicio, Date fin);

}
