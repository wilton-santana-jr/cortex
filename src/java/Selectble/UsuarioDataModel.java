package selectble;

import Interfaces.dao.IRepositorioUsuarios;
import Model.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

public class UsuarioDataModel extends ListDataModel<Usuario> implements Serializable, SelectableDataModel<Usuario> {

    private IRepositorioUsuarios usuarioDao;

    public UsuarioDataModel() {
    }

    public UsuarioDataModel(List<Usuario> data) {
        super(data);
    }
    
    public UsuarioDataModel(List<Usuario> data,IRepositorioUsuarios usuarioDao) {
        super(data);
        this.usuarioDao = usuarioDao;
    }

    @Override
    public Usuario getRowData(String rowKey) {
            Usuario user = usuarioDao.getUsuario(rowKey);
            if(user!=null)
                user.getAutorizacoes().size();
            return user;                         
    }

    @Override
    public Object getRowKey(Usuario user) {
        return user.getUsername();
    }  
    
    
    
    
}
