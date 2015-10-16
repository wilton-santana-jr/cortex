package Dao;

import Interfaces.dao.IRepositorioTipoUsuarios;
import Model.TipoUsuario;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class RepositorioTipoUsuariosBDR implements IRepositorioTipoUsuarios{


    
    @Override
    public List<TipoUsuario> list() {        
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<TipoUsuario> lista=null;
        try {
            Transaction t = session.beginTransaction();
            lista = session.createQuery("from TipoUsuario t order by t.descricao").list();
            t.commit();           
        } catch (Exception ex) {
            
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public TipoUsuario getTipoUsuario(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        TipoUsuario tipoUsuarioBD;        
        Transaction t = session.beginTransaction();
        try {                                   
            tipoUsuarioBD = (TipoUsuario) session.get(TipoUsuario.class, id);              
            t.commit();           
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do tipo de usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return tipoUsuarioBD;  
    }


}