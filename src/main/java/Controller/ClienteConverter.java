package Controller;

import Entity.Clientes;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@FacesConverter(value = "clienteConverter", managed = true)
public class ClienteConverter implements Converter<Clientes> {

    @Inject
    private ManagerClientes managerClientes;

    @Override
    public Clientes getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            Long id = Long.valueOf(value);
            return managerClientes.getListaClientes().stream()
                    .filter(c -> c.getId_cliente().equals(id))
                    .findFirst()
                    .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Clientes value) {
        if (value == null || value.getId_cliente() == null) {
            return "";
        }
        return String.valueOf(value.getId_cliente());
    }
}
