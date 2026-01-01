/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package EJB.Productos.Ventas;

import Entity.Ventas;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author juank
 */
@Local
public interface VentasFacadeLocal {

    void create(Ventas ventas);

    void edit(Ventas ventas);

    void remove(Ventas ventas);

    Ventas find(Object id);

    List<Ventas> findAll();

    List<Ventas> findRange(int[] range);
    
    int obtenerUltimoIdVenta();

    Double obtenerTotalVentas();
    
    List<Ventas> obtenerVentasConCliente();
            
    int count();
    
}
