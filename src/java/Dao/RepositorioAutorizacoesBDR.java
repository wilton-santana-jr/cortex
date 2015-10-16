package Dao;

import Interfaces.dao.IRepositorioAutorizacoes;
import Model.Autorizacao;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Wilton
 */
public class RepositorioAutorizacoesBDR implements IRepositorioAutorizacoes{    
    
    @Override
     public List<Autorizacao> list() {        
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Autorizacao> lista=null;
        Transaction t = session.beginTransaction();;
        try {            
            lista = session.createQuery("from Autorizacao t order by t.descricao").list();
            t.commit();           
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca das permissões de acesso. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }
     
    @Override
     public List<Autorizacao> list(String whereRolesName) {        
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Autorizacao> lista=null;
        Transaction t = session.beginTransaction();
        try {            
            lista = session.createQuery("from Autorizacao t where t.nome in ("+ whereRolesName+") order by t.descricao").list();
            t.commit();           
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca das permissões de acesso. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }

   
    
}
