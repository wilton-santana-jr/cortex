package Controller;

import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Usuario;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;



@ManagedBean
@RequestScoped
public class BoletoController implements Serializable {
    
    String  competencia;
    String  vencimento;   
    Usuario usuario;
    
    private IRepositorioUsuarios usuarioDao;

    public BoletoController() {        
        usuario = new Usuario();
        usuarioDao = new RepositorioUsuariosBDR();       
        
        setaVencimentoAndCompetencia();
        setaUsuarioLogado();
    } 
    
    public void obterBoletoByUsuario(String usuarioLogin) {              
       setaVencimentoAndCompetencia();              
       usuario = usuarioDao.getUsuario(usuarioLogin);                                                               
    }
    
    

    public String getCompetencia() {
        return competencia;
    }

    public String getVencimento() {
        return vencimento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public IRepositorioUsuarios getUsuarioDao() {
        return usuarioDao;
    }

    public void setUsuarioDao(RepositorioUsuariosBDR usuarioDao) {
        this.usuarioDao = usuarioDao;
    }
    
               
    private void setaVencimentoAndCompetencia() {
        DateFormat fmt = new SimpleDateFormat("MM/yyyy");         
        Calendar competenciaData = Calendar.getInstance();        
        competencia = fmt.format(competenciaData.getTime());
        
        
        DateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");         
        Calendar vencimentoData = Calendar.getInstance();               
        vencimentoData.add(Calendar.DAY_OF_YEAR, +1);
        
        
        
        boolean isSabado = vencimentoData.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY;
        boolean isDomingo = vencimentoData.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY;
        
        if(isSabado)
            vencimentoData.add(Calendar.DAY_OF_YEAR, +2);
        if(isDomingo)
            vencimentoData.add(Calendar.DAY_OF_YEAR, +1);
        
        
        vencimento = fmt2.format(vencimentoData.getTime());
    }

    private void setaUsuarioLogado() {
        SecurityContext context = SecurityContextHolder.getContext();
       if (context instanceof SecurityContext) {
           Authentication authentication = context.getAuthentication();
           if (authentication instanceof Authentication) {
               if (authentication.getPrincipal() != null) {
                   String userName = ((User) authentication.getPrincipal()).getUsername();
                   usuario = usuarioDao.getUsuario(userName);                    
                   usuario.setUsername(((User) authentication.getPrincipal()).getUsername());
               }
           }
       }
    }

    
    
    
    
    
}

