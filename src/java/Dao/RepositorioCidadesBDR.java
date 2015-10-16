package Dao;


import Interfaces.dao.IRepositorioCidades;
import Model.Cidade;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class RepositorioCidadesBDR implements IRepositorioCidades{


    
    @Override
    public List<Cidade> list() {        
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Cidade> lista=null;
        try {
            Transaction t = session.beginTransaction();
            lista = session.createQuery("from Cidade t order by t.nome").list();
            t.commit();           
        } catch (Exception ex) {
            
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public List<Cidade> listCidadesOfEstado(Long idEstado) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Cidade> lista=null;
        try {
            Transaction t = session.beginTransaction();
            lista = session.createQuery("from Cidade t where t.estado.id="+idEstado+" order by t.nome").list();
            t.commit();           
        } catch (Exception ex) {
            
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public Cidade getCidade(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Cidade cidadeBd;        
        Transaction t = session.beginTransaction();
        try {                                   
            cidadeBd = (Cidade) session.get(Cidade.class, id);              
            t.commit();           
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca da cidade. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return cidadeBd;  
    }

}
