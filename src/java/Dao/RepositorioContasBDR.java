package Dao;

import Interfaces.dao.IRepositorioContas;
import Model.Conta;
import Model.RegistroOperacao;
import Model.Usuario;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RepositorioContasBDR implements IRepositorioContas {

    @Override
    public BigDecimal getSaldo(Usuario usuarioSelecionado) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Conta contaBD;
        Transaction t = session.beginTransaction();
        try {
            contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                    + usuarioSelecionado.getUsername() + "\' order by t.usuario.username").uniqueResult();
            if (contaBD != null) {
                contaBD.getUsuario().getAutorizacoes().size();
            }
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do saldo da conta do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        if (contaBD != null) {
            return contaBD.getSaldo();
        } else {
            return null;
        }
    }

    @Override
    public List<RegistroOperacao> getExtrato(Usuario usuarioLogado, Calendar dataInicio, Calendar dataFim) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<RegistroOperacao> listRegistroOperacao;
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery("from RegistroOperacao t where " + "t.conta.usuario.username=\'"
                    + usuarioLogado.getUsername() + "\' and "
                    + " (t.dataRegistro between :dataIni and :dataFim) "
                    + "order by t.dataRegistro asc ");

            query.setCalendar("dataIni", dataInicio);
            query.setCalendar("dataFim", dataFim);

            listRegistroOperacao = query.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao gerar o extrato da conta do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        if (listRegistroOperacao != null) {
            return listRegistroOperacao;
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getSaldoByLogin(String login) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Conta contaBD;
        Transaction t = session.beginTransaction();
        try {
            contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                    + login + "\' order by t.usuario.username").uniqueResult();
            if (contaBD != null) {
                contaBD.getUsuario().getAutorizacoes().size();
            }
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do saldo da conta do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        if (contaBD != null) {
            return contaBD.getSaldo();
        } else {
            return null;
        }
    }

    @Override
    public Conta getContaByUsuario(Usuario selectedUsuario, Session session) {

        Conta contaBD;


        contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                + selectedUsuario.getUsername() + "\' order by t.usuario.username").uniqueResult();
        if (contaBD != null) {
            contaBD.getUsuario().getAutorizacoes().size();
        }
        if (contaBD != null) {
            return contaBD;
        } else {
            return null;
        }
    }

    //Verifica se conta de usuário existe se não tenta criar uma conta nova para o usuário selecionado
    @Override
    public boolean verificaContaDeUsuario(Usuario usuarioSelecionado) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Conta contaBD;
        Transaction t = session.beginTransaction();
        boolean sucesso = false;
        try {

            if (usuarioSelecionado != null && usuarioSelecionado.getUsername() != null && !usuarioSelecionado.getUsername().equalsIgnoreCase("")) {

                contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                        + usuarioSelecionado.getUsername() + "\' order by t.usuario.username").uniqueResult();
                if (contaBD != null) {
                    contaBD.getUsuario().getAutorizacoes().size();
                } else {
                    Conta novaConta = new Conta(usuarioSelecionado, new BigDecimal(0.0), Calendar.getInstance());
                    session.save(novaConta);
                    contaBD = novaConta;
                }
            }
            t.commit();
            sucesso = true;
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao tentar selecionar/criar a conta do usuário selecionado. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        return sucesso;
    }

    @Override
    public boolean boletoFoiPago(String numeroBoleto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegistroOperacao registroOperacaoBD = null;
        Transaction t = session.beginTransaction();
        try {

            if (numeroBoleto != null && !numeroBoleto.equalsIgnoreCase("")) {

                registroOperacaoBD = (RegistroOperacao) session.createQuery("from RegistroOperacao t where " + "t.boleto=\'"
                        + numeroBoleto + "\' order by t.id").uniqueResult();

            }
            t.commit();



        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao verificar se o boleto já foi pagou ou não. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }


        if (registroOperacaoBD != null) {
            return true;
        } else {
            return false;
        }


    }

    @Override
    public Conta getContaByUsuario(Usuario selectedUsuario) {


        Session session = HibernateUtil.getSessionFactory().openSession();
        Conta contaBD = null;
        Transaction t = session.beginTransaction();

        try {

            contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                    + selectedUsuario.getUsername() + "\' order by t.usuario.username").uniqueResult();
            if (contaBD != null) {
                contaBD.getUsuario().getAutorizacoes().size();
            }

            t.commit();


        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao buscar a conta do cliente selecionado. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        if (contaBD != null) {
            return contaBD;
        } else {
            return null;
        }
    
    }
    
    
     //Verifica se conta de usuário existe se não tenta criar uma conta nova para o usuário selecionado
    @Override
    public boolean verificaContaDeUsuario(Session session,Usuario usuarioSelecionado) {       
        Conta contaBD;       
        boolean sucesso = false;
        try {

            if (usuarioSelecionado != null && usuarioSelecionado.getUsername() != null && !usuarioSelecionado.getUsername().equalsIgnoreCase("")) {

                contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                        + usuarioSelecionado.getUsername() + "\' order by t.usuario.username").uniqueResult();
                if (contaBD != null) {
                    contaBD.getUsuario().getAutorizacoes().size();
                } else {
                    Conta novaConta = new Conta(usuarioSelecionado, new BigDecimal(0.0), Calendar.getInstance());
                    session.save(novaConta);
                    contaBD = novaConta;
                }
            }           
            sucesso = true;
        } catch (Exception ex) {            
            throw new RuntimeException("Ocorreu um erro ao tentar selecionar/criar a conta do usuário selecionado. Aguarde um momento e tente novamente mais tarde.");
        }

        return sucesso;
    }
    
    
    @Override
    public Conta getContaByUsuario(Session session, Usuario selectedUsuario) {      
        Conta contaBD = null;      
        try {

            contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                    + selectedUsuario.getUsername() + "\' order by t.usuario.username").uniqueResult();
            if (contaBD != null) {
                contaBD.getUsuario().getAutorizacoes().size();
            }
        } catch (Exception ex) {            
            throw new RuntimeException("Ocorreu um erro ao buscar a conta do cliente selecionado. Aguarde um momento e tente novamente mais tarde.");
        }

        if (contaBD != null) {
            return contaBD;
        } else {
            return null;
        }
    
    }
    
}
