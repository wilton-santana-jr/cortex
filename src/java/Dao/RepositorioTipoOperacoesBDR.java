/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Interfaces.dao.IRepositorioTipoOperacoes;
import Model.Conta;
import Model.RegistroOperacao;
import Model.TipoOperacao;
import Model.Usuario;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Wilton
 */
public class RepositorioTipoOperacoesBDR implements IRepositorioTipoOperacoes {

    
    /**
     * 
     * @param tipoOperacao C- Crédito / D- Débito
     * @return 
     */
    @Override
    public TipoOperacao getTipoOperacao(String tipoOperacao) {
       Session session = HibernateUtil.getSessionFactory().openSession();
        TipoOperacao tipoOperacaoBD;
        Transaction t = session.beginTransaction();
        try {
            tipoOperacaoBD = (TipoOperacao) session.createQuery("from TipoOperacao t where " + "t.abreviacao=\'" 
                    + tipoOperacao + "\' order by t.id").uniqueResult();
            
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do tipo de operação bancária (crédito/débito) no banco de dados. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        if (tipoOperacaoBD != null) {
            return tipoOperacaoBD;
        } else {
            return null;
        }
    }
    
    
    /**
     * 
     * @param tipoOperacao C- Crédito / D- Débito
     * @return 
     */
    @Override
    public TipoOperacao getTipoOperacao(Session session, String tipoOperacao) {       
        TipoOperacao tipoOperacaoBD;        
        try {
            tipoOperacaoBD = (TipoOperacao) session.createQuery("from TipoOperacao t where " + "t.abreviacao=\'" 
                    + tipoOperacao + "\' order by t.id").uniqueResult();                        
        } catch (Exception ex) {            
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do tipo de operação bancária (crédito/débito) no banco de dados. Aguarde um momento e tente novamente mais tarde.");
        }

        if (tipoOperacaoBD != null) {
            return tipoOperacaoBD;
        } else {
            return null;
        }
    }
    
}
