/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Clientes;

import Entity.Clientes;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author juank
 */
@Local
public interface ClientesFacadeLocal {

    void create(Clientes clientes);

    void edit(Clientes clientes);

    void remove(Clientes clientes);

    Clientes find(Object id);

    List<Clientes> findAll();

    List<Clientes> findRange(int[] range);

    int count();
    
    Clientes BuscarDui(String dui);
    
    List<Clientes> BuscarNombre(String nombre);
    
    List<Clientes> listarPrimeros10();
    
}
