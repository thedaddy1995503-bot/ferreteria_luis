package Controller;

import Entity.Medida;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import EJB.MedidaFacadeLocal;

@FacesConverter(value = "medidaConverter", managed = true)
public class MedidaConverter implements Converter<Medida> {

    @Inject
    private MedidaFacadeLocal medidaFCL;

    @Override
    public Medida getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return medidaFCL.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Medida value) {
        if (value == null || value.getId_medida() == null) {
            return "";
        }
        return String.valueOf(value.getId_medida());
    }
}
