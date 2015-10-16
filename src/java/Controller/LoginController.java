package Controller;

import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Usuario;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    private Usuario usuario; //representa o usuario logado
    private IRepositorioUsuarios usuarioDao;
    
    
    public LoginController() {
        usuario = new Usuario();
        usuarioDao = new RepositorioUsuariosBDR();
        
        
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                if (authentication.getPrincipal() != null) {
                    String userName = ((User) authentication.getPrincipal()).getUsername();
                    usuario = usuarioDao.getUsuario(userName);                    
                    //usuario.setUsername(userName);
                    //usuario.setUsername(((User) authentication.getPrincipal()).getUsername());
                }
            }
        }
    }

    
    public  Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
    
}
