package converter;

import Model.Autorizacao;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


//Usado para converter uma String que representa o nome de uma Autorizacao em um Objeto Autorizacao
//Util quando se trabalha com o componente inputManyCheckBox
public class AutorizacaoConverter implements Converter {        

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
            Autorizacao autorizacao = new Autorizacao();
            autorizacao.setNome(value);
            return autorizacao;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && value instanceof Autorizacao ) {
            return ((Autorizacao) value).getNome();
        }
        return "";
    }

}
