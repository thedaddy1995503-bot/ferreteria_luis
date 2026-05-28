package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Entidad que representa las unidades de medida (Kg, Unidad, Litro, etc.)
 * @author juank
 */
@Entity
@Table(name = "medida")
public class Medida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_medida;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    public Medida() {
    }

    public Medida(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_medida() {
        return id_medida;
    }

    public void setId_medida(Long id_medida) {
        this.id_medida = id_medida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_medida != null ? id_medida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Medida)) {
            return false;
        }
        Medida other = (Medida) object;
        if ((this.id_medida == null && other.id_medida != null) || (this.id_medida != null && !this.id_medida.equals(other.id_medida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Medida[ id=" + id_medida + " ]";
    }
}
