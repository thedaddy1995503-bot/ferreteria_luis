/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package EJB.Productos;

import Entity.Productos;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author juank
 */
@Local
public interface ProductosFacadeLocal {

    void create(Productos productos);

    void edit(Productos productos);

    void remove(Productos productos);

    Productos find(Object id);

    List<Productos> findAll();

    List<Productos> findRange(int[] range);

    int count();

    Productos BuscarNombre(String nombre);

    List<Productos> NombreProducto(String nombre);

    List<Productos> BuscarPorNombreParcial(String nombre);

    void actualizarStock(Long idProducto, Double cantidadVendida);

    Productos obtenerProductoConMenorInventario();

    List<Productos> listarPrimeros10();

}
