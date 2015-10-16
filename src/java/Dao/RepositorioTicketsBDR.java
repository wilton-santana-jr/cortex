package Dao;

import Interfaces.dao.IRepositorioTickets;
import Model.RegistroOperacao;
import Model.Ticket;
import Model.TipoRefeicao;
import Model.Usuario;
import Model.VendaTicket;
import Utils.HibernateUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RepositorioTicketsBDR implements IRepositorioTickets {

    @Override
    public void save(Ticket ticket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            session.save(ticket);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Ticket ticket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            session.update(ticket);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a atualização do ticket. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
    }

    @Override
    public void update(VendaTicket vendaTicketBd) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            session.update(vendaTicketBd);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a atualização da venda do ticket do usuário selecionado. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
    }

    @Override
    public Ticket getTicket(Long idTicket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Ticket ticketBd;
        Transaction t = session.beginTransaction();
        try {
            ticketBd = (Ticket) session.get(Ticket.class, idTicket);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do ticket. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return ticketBd;
    }

    @Override
    public Ticket getTicketByTipoAndDataRefeicao(TipoRefeicao tipoRefeicao, Date dataRefeicao) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Ticket ticketBd;
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery("from Ticket t where t.tipoRefeicao.abreviacao=:tipoRefeicao "
                    + "and t.dataRefeicao=:dataRefeicao order by t.dataRefeicao, t.tipoRefeicao.id");

            query.setString("tipoRefeicao", tipoRefeicao.getAbreviacao());
            query.setDate("dataRefeicao", dataRefeicao);

            ticketBd = (Ticket) query.uniqueResult();
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do ticket. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return ticketBd;
    }

    @Override
    public List<Ticket> getTicketsByDataRefeicao(Date dataRefeicao) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Ticket> lista;
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery("from Ticket t where t.dataRefeicao=:dataRefeicao "
                    + " order by t.tipoRefeicao.id");

            query.setDate("dataRefeicao", dataRefeicao);

            lista = query.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca dos tickets por data. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public List<Ticket> list() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Ticket> lista;
        Transaction t = session.beginTransaction();
        try {
//            Query query = session.createQuery("from Ticket t where t.dataRefeicao>=:dataRefeicao order by t.dataRefeicao desc, t.tipoRefeicao.id asc");
//            Calendar dataAtual = Calendar.getInstance();            
//            query.setDate("dataRefeicao", dataAtual.getTime());
//            lista = query.list();            

            SQLQuery querySql = session.createSQLQuery(""
                    + "select * from Ticket t "
                    + "where "
                    + "(t.dataRefeicao>=current_date)"
                    + " order by t.dataRefeicao desc, t.tipoRefeicaoid asc").addEntity(Ticket.class);

            lista = querySql.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem dos tickets cadastrados. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public boolean existeNoBanco(Date dataRefeicao, TipoRefeicao tipoRefeicao) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Ticket ticketBd;
        boolean retorno = false;
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery("from Ticket t where t.tipoRefeicao.abreviacao=:tipoRefeicao "
                    + "and t.dataRefeicao=:dataRefeicao order by t.dataRefeicao, t.tipoRefeicao.id");

            query.setString("tipoRefeicao", tipoRefeicao.getAbreviacao());
            query.setDate("dataRefeicao", dataRefeicao);

            ticketBd = (Ticket) query.uniqueResult();
            t.commit();

            if (ticketBd != null) {
                retorno = true;
            }
        } catch (Exception ex) {
            retorno = false;
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do ticket. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        return retorno;


    }

    @Override
    public boolean verificaSeAlguemJaComprouTicket(Ticket ticket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Ticket ticketBd;
        List<VendaTicket> vendaTicketBd;
        boolean retorno = false;
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery("from VendaTicket t where t.ticketComprado.id=:ticketId "
                    + " order by t.id");

            query.setLong("ticketId", ticket.getId());

            vendaTicketBd = query.list();
            t.commit();

            if (vendaTicketBd != null && !vendaTicketBd.isEmpty()) {
                retorno = true;
            }

        } catch (Exception ex) {
            retorno = false;
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca das vendas para o ticket informado. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        return retorno;
    }

    /**
     * Lista os tickets que ainda encontram-se disponíveis a venda
     *
     * @return
     */
    @Override
    public List<Ticket> listTicketsAVenda() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Ticket> lista;
        Transaction t = session.beginTransaction();
        try {

            SQLQuery querySql = session.createSQLQuery(""
                    + "select * from Ticket t "
                    + "where "
                    + "(t.dataRefeicao>=current_date)and"
                    + "(current_timestamp<=(horaInicioRefeicao-INTERVAL '1 hour 30 minutes')) "
                    + " order by t.dataRefeicao asc, t.tipoRefeicaoid asc").addEntity(Ticket.class);

            lista = querySql.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem dos tickets a venda cadastrados. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public VendaTicket getVendaTicket(Long idVendaTicket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        VendaTicket vendaTicketBd;
        Transaction t = session.beginTransaction();
        try {
            vendaTicketBd = (VendaTicket) session.get(VendaTicket.class, idVendaTicket);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar a busca da venda de determinado ticket. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return vendaTicketBd;
    }

    //Verifica se foi feita uma determinada venda de um determinado ticket para um determinado usuario
    @Override
    public VendaTicket getVendaTicket(VendaTicket vendaTicket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        VendaTicket vendaTicketBd;
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery("from VendaTicket t where t.contaComprador.usuario.username=:usuarioComprador "
                    + "and t.ticketComprado.id=:ticketComprado ");

            query.setString("usuarioComprador", vendaTicket.getContaComprador().getUsuario().getUsername());
            query.setLong("ticketComprado", vendaTicket.getTicketComprado().getId());

            vendaTicketBd = (VendaTicket) query.uniqueResult();
            t.commit();
        } catch (Exception ex) {
            t.rollback();                        
            throw new RuntimeException("Ocorreu um erro ao realizar a busca da venda do ticket para o usuário selecionado. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return vendaTicketBd;
    }

    //verifica se o ticketComprado na vendaTicket encontra-se disponível ainda para venda
    @Override
    public boolean estaNoPeriodoDeVenda(VendaTicket vendaTicket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Ticket ticketBd;
        Transaction t = session.beginTransaction();
        boolean retorno = false;
        try {

            SQLQuery querySql = session.createSQLQuery(""
                    + "select * from Ticket t "
                    + "where "
                    + "t.id=" + vendaTicket.getTicketComprado().getId() + " and "
                    + "(t.dataRefeicao>=current_date)and"
                    + "(current_timestamp<=(horaInicioRefeicao-INTERVAL '1 hour 30 minutes')) "
                    + " order by t.dataRefeicao desc, t.tipoRefeicaoid asc").addEntity(Ticket.class);

            ticketBd = (Ticket) querySql.uniqueResult();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na verificação de que se o ticket que está tentando ser comprado encontra-se ainda disponível para venda. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        if (ticketBd != null) {
            retorno = true;
        } else {
            retorno = false;
        }

        return retorno;
    }

    @Override
    public boolean verificaSeVendaTicketJaExiste(VendaTicket vendaTicket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        VendaTicket vendaTicketBd;
        Transaction t = session.beginTransaction();
        boolean retorno = false;
        try {
            Query query = session.createQuery("from VendaTicket t where t.contaComprador.usuario.username=:usuarioComprador "
                    + "and t.ticketComprado.id=:ticketComprado ");

            query.setString("usuarioComprador", vendaTicket.getContaComprador().getUsuario().getUsername());
            query.setLong("ticketComprado", vendaTicket.getTicketComprado().getId());

            vendaTicketBd = (VendaTicket) query.uniqueResult();
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            retorno = false;
            throw new RuntimeException("Ocorreu um erro ao realizar a busca da venda do ticket para o usuário selecionado. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

        if (vendaTicketBd != null) {
            retorno = true;
            vendaTicket.setId(vendaTicketBd.getId());

        } else {
            retorno = false;
        }

        return retorno;

    }
    
    
    
    @Override
     public boolean verificaSeVendaTicketJaExisteInTransaction(Session session,VendaTicket vendaTicket) {
        
        VendaTicket vendaTicketBd;      
        boolean retorno = false;
        try {
            Query query = session.createQuery("from VendaTicket t where t.contaComprador.usuario.username=:usuarioComprador "
                    + "and t.ticketComprado.id=:ticketComprado ");

            query.setString("usuarioComprador", vendaTicket.getContaComprador().getUsuario().getUsername());
            query.setLong("ticketComprado", vendaTicket.getTicketComprado().getId());

            vendaTicketBd = (VendaTicket) query.uniqueResult();
            session.evict(vendaTicketBd);
        } catch (Exception ex) {            
            retorno = false;
            throw new RuntimeException("Ocorreu um erro ao realizar a busca da venda do ticket para o usuário selecionado. Aguarde um momento e tente novamente mais tarde.");
        }

        if (vendaTicketBd != null) {
            retorno = true;
            vendaTicket.setId(vendaTicketBd.getId());

        } else {
            retorno = false;
        }

        return retorno;

    }

    //lista os tickets comprados para uma determinada refeição selecionada
    @Override
    public List<VendaTicket> listTicketsCompradosPorRefeicao(Long ticketId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<VendaTicket> lista;
        Transaction t = session.beginTransaction();
        try {

            Query query = session.createQuery(""
                    + "from VendaTicket t"
                    + " where "
                    + " t.ticketComprado.id=:ticketid"
                    + " order by t.contaComprador.usuario.nome, t.dataHoraCompra ");

            query.setLong("ticketid", ticketId);


            lista = query.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem dos tickets comprados para a refeição selecionada. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;

    }
    
    
    @Override
    public List<VendaTicket> listTicketsCompradosPorRefeicaoByExpressaoBusca(Long ticketId, String expressaoBusca) {
    Session session = HibernateUtil.getSessionFactory().openSession();
        List<VendaTicket> lista;
        Transaction t = session.beginTransaction();
        try {

            Query query = session.createQuery(""
                    + "from VendaTicket t"
                    + " where "
                    + " t.ticketComprado.id=:ticketid and "
                    + " (t.contaComprador.usuario.nome like :nome or"
                    + " t.contaComprador.usuario.matricula like :matricula or"
                    + " t.contaComprador.usuario.cpf like :cpf ) "
                    + " order by t.contaComprador.usuario.nome, t.dataHoraCompra ");

            query.setLong("ticketid", ticketId);
            query.setString("nome", "%"+expressaoBusca+"%");
            query.setString("matricula", "%"+expressaoBusca+"%");
            query.setString("cpf", "%"+expressaoBusca+"%");


            lista = query.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem dos tickets comprados para a refeição selecionada pelos parâmetros informados. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }
    

    //retornar o total de tickets vendidos para um determinado ticket selecionado
    @Override
    public Long getTotalTicketsVendidos(Long ticketId) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Long quantidade = new Long(0);
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery(""
                    + "select sum(t.quantidade) from VendaTicket t"
                    + " where "
                    + " t.ticketComprado.id=:ticketid"
                    + " group by t.ticketComprado.id ");

            query.setLong("ticketid", ticketId);


            quantidade = (Long) query.uniqueResult();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem da quantidade de tickets comprados para a refeição selecionada. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return quantidade;
    }
    
    @Override
    public Long getTotalTicketsVendidosByExpressaoBusca(Long ticketId, String expressaoBusca) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Long quantidade = new Long(0);
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery(""
                    + "select sum(t.quantidade) from VendaTicket t"
                    + " where "
                    + " t.ticketComprado.id=:ticketid and "
                    + " (t.contaComprador.usuario.nome like :nome or"
                    + " t.contaComprador.usuario.matricula like :matricula or"
                    + " t.contaComprador.usuario.cpf like :cpf ) "
                    + " group by t.ticketComprado.id "
                    + "");
                    
                    
            query.setLong("ticketid", ticketId);
            query.setString("nome", "%"+expressaoBusca+"%");
            query.setString("matricula", "%"+expressaoBusca+"%");
            query.setString("cpf", "%"+expressaoBusca+"%");           


            quantidade = (Long) query.uniqueResult();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem da quantidade de tickets comprados para a refeição selecionada pelos parâmtros informados. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return quantidade;
    }
    

    //verifica se o ticketComprado na vendaTicket encontra-se disponível ainda para venda
    @Override
    public boolean estaNoPeriodoDeVenda(Session session, VendaTicket vendaTicket) {
        Ticket ticketBd;
        boolean retorno = false;
        try {

            SQLQuery querySql = session.createSQLQuery(""
                    + "select * from Ticket t "
                    + "where "
                    + "t.id=" + vendaTicket.getTicketComprado().getId() + " and "
                    + "(t.dataRefeicao>=current_date)and"
                    + "(current_timestamp<=(horaInicioRefeicao-INTERVAL '1 hour 30 minutes')) "
                    + " order by t.dataRefeicao desc, t.tipoRefeicaoid asc").addEntity(Ticket.class);

            ticketBd = (Ticket) querySql.uniqueResult();
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na verificação de que se o ticket que está tentando ser comprado encontra-se ainda disponível para venda. Aguarde um momento e tente novamente mais tarde.");
        }

        if (ticketBd != null) {
            retorno = true;
        } else {
            retorno = false;
        }

        return retorno;
    }

    //Verifica se foi feita uma determinada venda de um determinado ticket para um determinado usuario
    @Override
    public VendaTicket getVendaTicket(Session session, VendaTicket vendaTicket) {
        VendaTicket vendaTicketBd;
        try {
            Query query = session.createQuery("from VendaTicket t where "
                    + "t.contaComprador.usuario.username=:usuarioComprador "
                    + "and t.ticketComprado.id=:ticketComprado ");

            query.setString("usuarioComprador", vendaTicket.getContaComprador().getUsuario().getUsername());
            query.setLong("ticketComprado", vendaTicket.getTicketComprado().getId());

            vendaTicketBd = (VendaTicket) query.uniqueResult();
        } catch (Exception ex) {            
            throw new RuntimeException("Ocorreu um erro ao realizar a busca da venda do ticket para o usuário selecionado. Aguarde um momento e tente novamente mais tarde.");
        }
        return vendaTicketBd;
    }

    @Override
    public Ticket getTicket(Session session, Long idTicket) {
        Ticket ticketBd;
        try {
            ticketBd = (Ticket) session.get(Ticket.class, idTicket);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao realizar a busca do ticket. Aguarde um momento e tente novamente mais tarde.");
        }
        return ticketBd;
    }

    //Verificar se a horacorrente esta entre o (horario inicial da refeicao-10 minutes) e
    //o horario final da refeicao
    //se não estiver informar que o tempo para utilização do ticket se esgotou e o mesmo não poderá mais ser utilizado
    //você deveria ter utilizado o mesmo no período de horário da refeição
    @Override
    public boolean verificaSeVendaTicketEstaAptoAEntrarNoRefeitorioParaComer(VendaTicket vendaTicket) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Ticket ticket=null;
        Transaction t = session.beginTransaction();
        boolean apto = false;
        try {

            SQLQuery querySql = session.createSQLQuery(""
                    + "select * from Ticket t "
                    + "where "
                    +" t.id="+vendaTicket.getTicketComprado().getId()
                    + " and (t.dataRefeicao>=current_date) and "
                    + "(current_timestamp>=(horaInicioRefeicao-INTERVAL '10 minutes')) and"
                    + "(current_timestamp<=(horaFimRefeicao)) "
                    + " order by t.dataRefeicao desc, t.tipoRefeicaoid asc").addEntity(Ticket.class);

            ticket = (Ticket) querySql.uniqueResult();
            
            
            t.commit();
            
            if(ticket!=null){
                apto=true;
            }else{
                apto=false;
            }
        } catch (Exception ex) {
            t.rollback();
            apto = false;
            throw new RuntimeException("Ocorreu um erro na verificação se o ticket comprado pelo usuário expirou. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return apto;
    }

    @Override
    public List<Ticket> listTicketsCardapioDaSemana() {
         Session session = HibernateUtil.getSessionFactory().openSession();
        List<Ticket> lista;
        Transaction t = session.beginTransaction();
        try {
            
            Calendar dataAtual = Calendar.getInstance();
            int diaSemana = dataAtual.get(Calendar.DAY_OF_WEEK);  
            
            String whereTicketsDaSemana="";
            if(1==diaSemana){
                //domingo
                whereTicketsDaSemana="(t.dataRefeicao>=current_date-INTERVAL '0 days')and";
                whereTicketsDaSemana+="(t.dataRefeicao<=current_date+INTERVAL '6 days')";
            }else
            if(2==diaSemana){
                //segunda
                whereTicketsDaSemana="(t.dataRefeicao>=current_date-INTERVAL '1 days')and";
                whereTicketsDaSemana+="(t.dataRefeicao<=current_date+INTERVAL '5 days')";
            }else
            if(3==diaSemana){
                //terça
                whereTicketsDaSemana="(t.dataRefeicao>=current_date-INTERVAL '2 days')and";
                whereTicketsDaSemana+="(t.dataRefeicao<=current_date+INTERVAL '4 days')";
            }else
            if(4==diaSemana){
                //quarta
                whereTicketsDaSemana="(t.dataRefeicao>=current_date-INTERVAL '3 days')and";
                whereTicketsDaSemana+="(t.dataRefeicao<=current_date+INTERVAL '3 days')";
            }else
            if(5==diaSemana){
                //quinta
                whereTicketsDaSemana="(t.dataRefeicao>=current_date-INTERVAL '4 days')and";
               whereTicketsDaSemana+="(t.dataRefeicao<=current_date+INTERVAL '2 days')";
            }else
            if(6==diaSemana){
                //sexta
                whereTicketsDaSemana="(t.dataRefeicao>=current_date-INTERVAL '5 days')and";
                whereTicketsDaSemana+="(t.dataRefeicao<=current_date+INTERVAL '1 days')";
            }else
            if(7==diaSemana){
                //sábado
                whereTicketsDaSemana="(t.dataRefeicao>=current_date-INTERVAL '6 days')and";
                whereTicketsDaSemana+="(t.dataRefeicao<=current_date+INTERVAL '0 days')";
            }
                       
            

            //Retornará os tickets da semana(Cardápio da Semana)
            SQLQuery querySql = session.createSQLQuery(""
                    + "select * from Ticket t "
                    + "where "
                    + whereTicketsDaSemana                    
                    +" and (current_timestamp<=(horaInicioRefeicao-INTERVAL '1 hour 30 minutes')) "
                    + " order by t.dataRefeicao asc, t.tipoRefeicaoid asc").addEntity(Ticket.class);

            lista = querySql.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na listagem do cardápio da semana. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }

    @Override
    public Integer getQuantidadeCompradaDeTicketsPeloUsuarioPorRefeicao(Long ticketId, Usuario usuarioLogado) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Integer quantidade = 0;
        VendaTicket vendaTicket;
        Transaction t = session.beginTransaction();
        try {
            Query query = session.createQuery(""
                    + "select t from VendaTicket t"
                    + " where "
                    + " t.ticketComprado.id=:ticketid and "
                    + " t.contaComprador.usuario.username=:username "
                    + "");
                    
                    
            query.setLong("ticketid", ticketId);
            query.setString("username", usuarioLogado.getUsername());
            

            vendaTicket = (VendaTicket) query.uniqueResult();
            if(vendaTicket!=null){
                quantidade = vendaTicket.getQuantidade();
            }
            

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            quantidade=0;
            throw new RuntimeException("Ocorreu um erro na contagem de tickets comprados pelo usuario para a refeição selecionada. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return quantidade;
    }

    
    
 
    
    
    @Override
     public List<RegistroOperacao> relatorioBoletosCreditadosContaDosUsuarios(Date dataFormIni, Date dataFormFim) {
            
         Calendar dataInicio = Calendar.getInstance();
         Calendar dataFim = Calendar.getInstance();
            
        try{
            dataInicio.setTime(dataFormIni);
            dataInicio.set(Calendar.HOUR_OF_DAY, 0);
            dataInicio.set(Calendar.MINUTE, 0);
            dataInicio.set(Calendar.SECOND, 0);

            dataFim.setTime(dataFormFim);
            dataFim.set(Calendar.HOUR_OF_DAY, 24);
            dataFim.set(Calendar.MINUTE, 59);
            dataFim.set(Calendar.SECOND, 59);
        }catch (Exception ex) {            
            return null;
        }
        
         Session session = HibernateUtil.getSessionFactory().openSession();
        List<RegistroOperacao> lista;
        Transaction t = session.beginTransaction();
        try {
            
             
              Query query = session.createQuery(""
                    + "select t from RegistroOperacao t"
                    + " where "
                    + " t.tipoOperacao.abreviacao='C' and "
                    + " t.boleto is not null and"
                    + " (t.dataRegistro between :dataInicio and :dataFim) "  
                    + " order by t.dataRegistro asc, t.conta.usuario.nome asc, t.valor asc "
                    + "");
                    
                    
            query.setCalendar("dataInicio", dataInicio);
            query.setCalendar("dataFim", dataFim);

            

            lista = query.list();

            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro na geração do relatório de boletos creditados nas contas dos usuários para o período informado. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
        return lista;
    }
   

    

    
}
