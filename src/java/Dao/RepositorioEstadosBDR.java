package Dao;


import Interfaces.dao.IRepositorioEstados;
import Model.Estado;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class RepositorioEstadosBDR implements IRepositorioEstados{


    
    @Override
    public List<Estado> list() {        
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Estado> lista=null;
        try {
            Transaction t = session.beginTransaction();
            lista = session.createQuery("from Estado t order by t.nome").list();
            t.commit();           
        } catch (Exception ex) {
            
        } finally {
            session.close();
        }
        return lista;
    }

}