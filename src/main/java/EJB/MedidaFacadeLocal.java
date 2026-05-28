package EJB;

import Entity.Medida;
import jakarta.ejb.Local;
import java.util.List;

/**
 * @author juank
 */
@Local
public interface MedidaFacadeLocal {

    void create(Medida medida);

    void edit(Medida medida);

    void remove(Medida medida);

    Medida find(Object id);

    List<Medida> findAll();

    List<Medida> findRange(int[] range);

    int count();
    
}
