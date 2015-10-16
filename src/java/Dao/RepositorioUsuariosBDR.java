package Dao;

import Interfaces.dao.IRepositorioUsuarios;
import Model.Autorizacao;
import Model.Conta;
import Model.Usuario;
import Model.VendaTicket;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RepositorioUsuariosBDR implements IRepositorioUsuarios {

    @Override
    public void savePreCadastro(Usuario usuario) {

        //aciona a camada de negocio para salvar o cliente 
        usuario.setEnable(false);
        usuario.setDataBloqueio(Calendar.getInstance());
        usuario.setMotivoBloqueio("Pré-Cadastro no sistema");
        usuario.getAutorizacoes().clear();
        usuario.getAutorizacoes().add(new Autorizacao("ROLE_COMUM"));
        usuario.getAutorizacoes().add(new Autorizacao("ROLE_BOLETO"));

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            session.save(usuario);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o pré-cadastro do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            session.save(usuario);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            session.update(usuario);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a atualização do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
    }

    @Override
    public Usuario getUsuario(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuario usuarioBd;
        Transaction t = session.beginTransaction();
        try {
            usuarioBd = (Usuario) session.get(Usuario.class, username);
            if (usuarioBd != null) {
                usuarioBd.getAutorizacoes().size();
            }
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return usuarioBd;
    }

    @Override
    public Usuario getUsuarioByCPF(String cpf) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuario usuarioBd;
        Transaction t = session.beginTransaction();
        try {
            usuarioBd = (Usuario) session.createQuery("from Usuario t where " + "t.cpf=\'" + cpf + "\' order by t.nome").uniqueResult();
            if (usuarioBd != null) {
                usuarioBd.getAutorizacoes().size();
            }
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return usuarioBd;
    }

    @Override
    public Usuario getUsuarioByMatricula(String matricula) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuario usuarioBd;
        Transaction t = session.beginTransaction();
        try {
            usuarioBd = (Usuario) session.createQuery("from Usuario t where " + "t.matricula=\'" + matricula + "\' order by t.nome").uniqueResult();
            if (usuarioBd != null) {
                usuarioBd.getAutorizacoes().size();
            }
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return usuarioBd;
    }

    @Override
    public List<Usuario> list() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuario> lista;
        Transaction t = session.beginTransaction();
        try {
            lista = session.createQuery("from Usuario t order by t.nome").list();
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem dos usuários. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public boolean isUserPossuiRole(Autorizacao role, Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuario usuarioBd;
        Transaction t = session.beginTransaction();
        boolean retorno = false;
        try {

            String role_averiguado = "(select auto from Autorizacao auto where auto.nome=\'" + role.getNome() + "\')";

            usuarioBd = (Usuario) session.createQuery("select t from Usuario t where " + "t.username=\'" + usuario.getUsername() + "\'"
                    + " and size(t.autorizacoes) > 0 "
                    + " and " + role_averiguado + " in elements(t.autorizacoes) "
                    + " order by t.nome").uniqueResult();
            if (usuarioBd != null) {
                retorno = true;
            }

            t.commit();
        } catch (Exception ex) {
            retorno = false;
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do usuário. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return retorno;
    }

    @Override
    public boolean isUserPossuiRole(Session session, Autorizacao role, Usuario usuario) {

        Usuario usuarioBd;
        boolean retorno = false;
        try {

            String role_averiguado = "(select auto from Autorizacao auto where auto.nome=\'" + role.getNome() + "\')";

            usuarioBd = (Usuario) session.createQuery("select t from Usuario t where " + "t.username=\'" + usuario.getUsername() + "\'"
                    + " and size(t.autorizacoes) > 0 "
                    + " and " + role_averiguado + " in elements(t.autorizacoes) "
                    + " order by t.nome").uniqueResult();
            if (usuarioBd != null) {
                retorno = true;
            }

        } catch (Exception ex) {
            retorno = false;
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do usuário. Aguarde um momento e tente novamente mais tarde.");
        }
        return retorno;
    }

    @Override
    public Usuario getUsuario(Session session, String username) {
        Usuario usuarioBd;
        try {
            usuarioBd = (Usuario) session.get(Usuario.class, username);
            if (usuarioBd != null) {
                usuarioBd.getAutorizacoes().size();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do usuário. Aguarde um momento e tente novamente mais tarde.");
        }
        return usuarioBd;
    }

    //Retorna a lista de usuarios que possuem aas roles informadas em listaInAutorizacao
    // e que não possuem as roles informadas em listaNotInAutorizacao    
    @Override
    public List<Usuario> listUsuariosComAutorizacoesNaoPermitidasAndAutorizacoesPermitidas(List<Autorizacao> listaNotInAutorizacao, List<Autorizacao> listaInAutorizacao) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuario> lista;
        Transaction t = session.beginTransaction();
        try {

            String[] roleIn;
            roleIn = new String[listaInAutorizacao.size()];
            int inc_in = 0;
            String whereInRole = "";
            for (Autorizacao autorizacao : listaInAutorizacao) {
                roleIn[inc_in] = " (select auto" + inc_in + ".nome from Autorizacao auto" + inc_in + " where auto" + inc_in + ".nome=\'" + autorizacao.getNome() + "\') ";
                whereInRole = whereInRole + " and " + roleIn[inc_in] + " in elements(t.autorizacoes) ";
                inc_in++;
            }

            String[] roleNotInf;
            roleNotInf = new String[listaNotInAutorizacao.size()];
            int inc_notin = 0;
            String whereNotInRole = "";
            for (Autorizacao autorizacaoNotIn : listaNotInAutorizacao) {
                roleNotInf[inc_notin] = " (select auto" + inc_in + "" + inc_notin + ".nome from Autorizacao auto" + inc_in + "" + inc_notin + " where auto" + inc_in + "" + inc_notin + ".nome=\'" + autorizacaoNotIn.getNome() + "\') ";

                whereNotInRole = whereNotInRole + " and " + roleNotInf[inc_notin] + " not in elements(t.autorizacoes) ";
                inc_notin++;
            }

            lista = session.createQuery("select t from Usuario t where "
                    + " size(t.autorizacoes) > 0 "
                    + whereInRole
                    + whereNotInRole
                    + " order by t.nome").list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem dos usuários. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;

    }

    //Cria a conta do usuario se ela ainda não existir
    //verificar se o usuário criado possui conta 
    //se não tenta criar  uma nova conta para o usuário selecionado;
    @Override
    public boolean criaContaDoUsuario(Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Conta contaBD;
        Transaction t = session.beginTransaction();
        boolean sucesso = false;
        try {

            if (usuario != null && usuario.getUsername() != null && !usuario.getUsername().equalsIgnoreCase("")) {

                contaBD = (Conta) session.createQuery("from Conta t where " + "t.usuario.username=\'"
                        + usuario.getUsername() + "\' order by t.usuario.username").uniqueResult();
                if (contaBD != null) {
                    contaBD.getUsuario().getAutorizacoes().size();
                } else {
                    Conta novaConta = new Conta(usuario, new BigDecimal(0.0), Calendar.getInstance());
                    session.save(novaConta);
                    contaBD = novaConta;
                }
            }
            t.commit();
            sucesso = true;
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao tentar selecionar/criar a conta do usuário selecionado. Entre em contato com o administrador do sistema.");
        } finally {
            session.close();
        }

        return sucesso;
    }

    //Método sera chamado atraves do escalonadrod e tarefas quartz todos os dias nos horários
    //14:30 e 22:30 e verificará se os usuários do tipo aluno adiquiriu algum ticket de refeição para o 
    //dia corrente e não deu baixa do ticket no refeitorio 
    public static boolean bloqueiaAlunosQueAdquiremTicketsAndNaoUtilizamOsTickets() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<VendaTicket> listaTicketVendidosENãoUsadosPelosAlunos;
        Transaction t = session.beginTransaction();
        boolean sucesso = false;
        try {

            //Carregar uma lista com todas as vendaTickets dos alunos para o dia corrente
            //que tem seu status false ou seja que não foram utilizadas 
            // e que a horaFim da refeicao do ticket Comprado seja menor que a horaCorrente do sistema
            //Esta lista representará todos os tickets que foram adiquiridos e não foram usados pelos aluno                        
            SQLQuery querySql = session.createSQLQuery(""
                    + "select  vTic.id,vTic.dataHoraCompra,vTic.quantidade,vTic.status,vTic.contaid,vTic.regoperacaoid,vTic.ticketid,vTic.usuarioid "
                    + "from VendaTicket vTic, Conta conta, RegistroOperacao regOp, Usuario usu, TipoUsuario tipUsu, Ticket t  "
                    + "where "
                    + " vTic.contaid=conta.id and "
                    + " vTic.regoperacaoid=regOp.id and "
                    + " vTic.ticketid=t.id and "
                    + " vTic.usuarioid=usu.username and "
                    + " usu.tipousuarioid=tipUsu.id and "
                    + " vTic.status=false and "
                    + "(t.dataRefeicao=current_date) and "
                    + "(current_timestamp>(t.horaFimRefeicao)) "
                    + " order by vTic.id desc , vTic.usuarioId desc ").addEntity(VendaTicket.class);

            listaTicketVendidosENãoUsadosPelosAlunos = querySql.list();

            //Para a lista que foi gerada anteriormente atualizar os status dos clientes em lote
            // para o status disable assim o cliente ficará desabilitado no sistema e so será habilitado
            // quando se dirigir a lojinha e se justificar por que comprou o ticket e não usou
            for (Iterator<VendaTicket> it = listaTicketVendidosENãoUsadosPelosAlunos.iterator(); it.hasNext();) {
                VendaTicket vendaTicket = it.next();
                vendaTicket.getContaComprador().getUsuario().setEnable(false);
                session.update(vendaTicket);
            }
            t.commit();
            sucesso = true;
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao tentar bloquear usuários alunos que compram tickets e não dão baixa no refeitório.");
        } finally {
            session.close();
        }

        return sucesso;

    }

    public static boolean bloqueiaAlunosQueAdiquiriremDoisOuMaisTicketsAndNaoUtilizaremOsTickets() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuario> listaTicketVendidosENãoUsadosPelosAlunos;
        Transaction t = session.beginTransaction();
        boolean sucesso = false;
        try {

            // selecionar os usuarios que compraram dois ou mais tickets e não usuaram no refeitorio  
            SQLQuery querySql = session.createSQLQuery("select usu.username, usu.complemento, usu.bairro, usu.cep, usu.cpf, usu.email, usu.enable, usu.fone1, usu.fone2, usu.fone3, "
                    + " usu.logradouro, usu.matricula, usu.nome, usu.numerodaresidencia, usu.password, usu.sexo, usu.cidaderesidenciaid, "
                    + " usu.tipousuarioid,usu.databloqueio,usu.motivobloqueio   "
                    + " from VendaTicket vTic, Conta conta, RegistroOperacao regOp, Usuario usu, TipoUsuario tipUsu, Ticket t "
                    + " where                    "
                    + "  vTic.contaid=conta.id and "
                    + " vTic.regoperacaoid=regOp.id and "
                    + " vTic.ticketid=t.id and "
                    + " vTic.usuarioid=usu.username and  "
                    + " usu.enable=true and "
                    + " usu.tipousuarioid=tipUsu.id and   "
                    + " vTic.status=false and "
                    + " databloqueio<=datahoracompra and  "
                    + " (t.dataRefeicao<=current_date) and "
                    + " (current_timestamp>(t.horaFimRefeicao)) "
                    + " group by usu.username, usu.complemento, usu.bairro, usu.cep, usu.cpf, usu.email, usu.enable, usu.fone1, usu.fone2, usu.fone3, "
                    + " usu.logradouro, usu.matricula, usu.nome, usu.numerodaresidencia, usu.password, usu.sexo, usu.cidaderesidenciaid, "
                    + " usu.tipousuarioid,usu.databloqueio,usu.motivobloqueio                     "
                    + " having     count(vTic.usuarioId)>=2 "
                    + " order by usu.username desc ").addEntity(Usuario.class);

            listaTicketVendidosENãoUsadosPelosAlunos = querySql.list();

            //Para a lista que foi gerada anteriormente atualizar os status dos clientes em lote
            // para o status disable assim o cliente ficará desabilitado no sistema e so será habilitado
            // quando se dirigir a lojinha e se justificar por que comprou o ticket e não usou
            for (Iterator<Usuario> it = listaTicketVendidosENãoUsadosPelosAlunos.iterator(); it.hasNext();) {
                Usuario usuario = it.next();
                usuario.setEnable(false);
                usuario.setDataBloqueio(Calendar.getInstance());
                usuario.setMotivoBloqueio("Solicitação de ticket e não uso do mesmo");
                session.update(usuario);
            }
            t.commit();
            sucesso = true;
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao tentar bloquear usuários que compram ou solicitaram tickets e não deram baixa no refeitório.");
        } finally {
            session.close();
        }

        return sucesso;

    }
}
