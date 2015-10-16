package converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

//Usado para converter um valor numérico em BigDecimal para um formator monetário com o símbolo R$
//Util quando se trabalha com o retornos de valores BigDecimal e agente quer formatar esses valores de forma monetaria na visao
public class ValorMonetarioConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            BigDecimal numero = new BigDecimal(value);
            return numero;
        } catch (Exception ex) {
            return new BigDecimal(0.00);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        try {
            if (value != null && value instanceof BigDecimal) {
                return NumberFormat.getCurrencyInstance().format(((BigDecimal) value).doubleValue());
            } else {
                return "R$ 0,00";
            }
        } catch (Exception ex) {
            return "R$ 0,00";
        }

    }
}
