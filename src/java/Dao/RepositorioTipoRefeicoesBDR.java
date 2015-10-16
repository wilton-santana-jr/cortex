package Dao;

import Interfaces.dao.IRepositorioTipoRefeicoes;
import Model.TipoRefeicao;
import Model.TipoUsuario;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RepositorioTipoRefeicoesBDR implements IRepositorioTipoRefeicoes {

    @Override
    public List<TipoRefeicao> list() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<TipoRefeicao> lista = null;
        try {
            Transaction t = session.beginTransaction();
            lista = session.createQuery("from TipoRefeicao t order by t.nome").list();
            t.commit();
        } catch (Exception ex) {
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public TipoRefeicao getTipoRefeicao(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        TipoRefeicao tipoRefeicaoBd;
        Transaction t = session.beginTransaction();
        try {
            tipoRefeicaoBd = (TipoRefeicao) session.get(TipoRefeicao.class, id);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do tipo de refeição. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return tipoRefeicaoBd;
    }

    @Override
    public boolean isValida(TipoRefeicao tipoRefeicao) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        TipoRefeicao tipoRefeicaoBd;
        Transaction t = session.beginTransaction();
        boolean retorno = false;
        try {
            if (tipoRefeicao != null && tipoRefeicao.getId() != null) {
                tipoRefeicaoBd = (TipoRefeicao) session.get(TipoRefeicao.class, tipoRefeicao.getId());
                if (tipoRefeicaoBd != null) {
                    retorno = true;
                }
            }
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            retorno = false;
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do tipo de refeição. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return retorno;
    }
}