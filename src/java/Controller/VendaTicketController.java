package Controller;

import Dao.RepositorioContasBDR;
import Dao.RepositorioTicketsBDR;
import Dao.RepositorioTipoOperacoesBDR;
import Dao.RepositorioTipoUsuariosBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioContas;
import Interfaces.dao.IRepositorioTickets;
import Interfaces.dao.IRepositorioTipoOperacoes;
import Interfaces.dao.IRepositorioTipoUsuarios;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Conta;
import Model.Ticket;
import Model.TipoUsuario;
import Model.Usuario;
import Model.VendaTicket;
import Utils.HibernateUtil;
import Validator.VendaTicketValidator;
import Interfaces.validator.IVendaTicketValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import selectble.TicketDataModel;
import selectble.VendaTicketDataModel;

@ManagedBean
@ViewScoped
public class VendaTicketController implements Serializable {

    private Usuario selectedUsuario;
    private Ticket selectedTicket;
    private VendaTicket vendaTicket;
    private VendaTicket selectedVendaTicket;
    private Long qtdTotalTicketsVendidos;
    private String expressaoBusca;
    private String semanaCardapio;
    private String valoresTicketSemana;
    private VendaTicketDataModel vendaTicketDataModel;
    private TicketDataModel ticketDataModel;
    private List<Ticket> listTickets;
    private List<Ticket> listTicketsDaSemana;
//    private List<Integer> listQuantidade;
    private List<VendaTicket> listVendasTickets;
    private List<VendaTicket> filteredVendaTickets;
    private List<Boolean> listStatus;
    private IRepositorioUsuarios usuarioDao = new RepositorioUsuariosBDR();
    private IRepositorioTickets ticketDao = new RepositorioTicketsBDR();
    private IRepositorioContas contaDao = new RepositorioContasBDR();
    private IRepositorioTipoOperacoes tipoOperacaoDao = new RepositorioTipoOperacoesBDR();
    private IRepositorioTipoUsuarios tipoUsuarioDao = new RepositorioTipoUsuariosBDR();
    private IVendaTicketValidator vendaTicketValidator;

    public VendaTicketController() {

        selectedUsuario = new Usuario();
        vendaTicket = new VendaTicket();
        selectedTicket = new Ticket();
        selectedTicket.setId(new Long(0));
        selectedVendaTicket = new VendaTicket();
        expressaoBusca = (expressaoBusca != null && !expressaoBusca.equalsIgnoreCase("")) ? expressaoBusca : "";
        semanaCardapio = "";
        valoresTicketSemana = "";

        qtdTotalTicketsVendidos = new Long(0);

        listTickets = new ArrayList<>();
        listTicketsDaSemana = new ArrayList<>();

        listVendasTickets = new ArrayList<>();
        listStatus = new ArrayList<>();

        usuarioDao = new RepositorioUsuariosBDR();

        ticketDao = new RepositorioTicketsBDR();
        contaDao = new RepositorioContasBDR();
        tipoOperacaoDao = new RepositorioTipoOperacoesBDR();
        tipoUsuarioDao = new RepositorioTipoUsuariosBDR();
        vendaTicketValidator = new VendaTicketValidator();


        listTickets = ticketDao.listTicketsCardapioDaSemana();//pega todos os tickets disponiveis para venda 
        listTicketsDaSemana = ticketDao.listTicketsCardapioDaSemana();//pega todos os tickets a venda disponíveis da semana




        listStatus.add(true);
        listStatus.add(false);

    }


    public void carregarTicketsCompradosPorRefeicao(Long ticketId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            expressaoBusca = (expressaoBusca != null && !expressaoBusca.equalsIgnoreCase("")) ? expressaoBusca : "";
            if (ticketId != null) {
                selectedTicket = ticketDao.getTicket(ticketId);
                qtdTotalTicketsVendidos = ticketDao.getTotalTicketsVendidos(ticketId);

                try {
                    List<VendaTicket> lista = ticketDao.listTicketsCompradosPorRefeicao(selectedTicket.getId());
                    vendaTicketDataModel = new VendaTicketDataModel(lista, ticketDao);
                    filteredVendaTickets = ticketDao.listTicketsCompradosPorRefeicaoByExpressaoBusca(selectedTicket.getId(), expressaoBusca);

                } catch (Exception ex) {
                    vendaTicketDataModel = null;
                    filteredVendaTickets = null;
                }

            } else {
                selectedTicket = new Ticket();
                selectedTicket.setId(new Long(0));
                qtdTotalTicketsVendidos = new Long(0);
                vendaTicketDataModel = null;
                filteredVendaTickets = null;                
                throw new RuntimeException("Selecione a refeição corretamente para carregar os tickets solicitados/comprados para está refeição.");
            }
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }
    }

    public void filtraVendasTicketByExpression(Long idTicket) {

        try {
            selectedTicket = ticketDao.getTicket(idTicket);

            List<VendaTicket> lista = ticketDao.listTicketsCompradosPorRefeicaoByExpressaoBusca(selectedTicket.getId(), expressaoBusca);
            vendaTicketDataModel = new VendaTicketDataModel(lista, ticketDao);
            filteredVendaTickets = ticketDao.listTicketsCompradosPorRefeicaoByExpressaoBusca(idTicket, expressaoBusca);
            setQtdTotalTicketsVendidos(ticketDao.getTotalTicketsVendidosByExpressaoBusca(selectedTicket.getId(), expressaoBusca));
            if (filteredVendaTickets != null && !filteredVendaTickets.isEmpty()) {
                selectedVendaTicket = filteredVendaTickets.get(0);
                vendaTicket = filteredVendaTickets.get(0);
            }
        } catch (Exception ex) {
            vendaTicketDataModel = null;
        }

    }

    public void alterarStatusVendaTicket() {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {


            VendaTicket vendaTicketBd = ticketDao.getVendaTicket(vendaTicket.getId());

            vendaTicketBd.setStatus(vendaTicket.isStatus());


            vendaTicketValidator.onUpdateStatusVendaTicket(vendaTicket, ticketDao);

            ticketDao.update(vendaTicketBd);

            String situacao = (vendaTicketBd.isStatus() == false) ? " \"Não Usado\" " : "\"Usado\"";
            String result = (vendaTicketBd.isStatus() == true) ? " poderá adentrar ao refeitório e fazer um bom apetite da refeição. " : " não poderá entrar no refeitório.";
            String msg = "Status da Venda do ticket comprado para o " + vendaTicketBd.getTicketComprado() + " pelo  usuário: " + vendaTicketBd.getContaComprador().getUsuario().getNome() + " foi alterado para o status de"
                    + situacao
                    + "! Sendo assim o usuário "
                    + result;
            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));

            if (filteredVendaTickets != null) {
                int index = filteredVendaTickets.indexOf(vendaTicket); //captura o indice do ticket com dados antigo na lista de filtrados
                filteredVendaTickets.remove(index); //remove o ticket com dados antigo da lista de filtrados
                filteredVendaTickets.add(index, vendaTicket);  //adiciona o novo ticket com seus novos dados na lista de filtrados              
            }

        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }
    }

    //Altera o Status da Venda Ticket
    public void alterarStatusVendaTicket(Long vendaTicketId) {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {


            VendaTicket vendaTicketBd = ticketDao.getVendaTicket(vendaTicketId);

            boolean status = vendaTicketBd.isStatus();

            vendaTicketBd.setStatus(!status);

            vendaTicketValidator.onUpdateStatusVendaTicket(vendaTicketBd, ticketDao);

            ticketDao.update(vendaTicketBd);

            this.vendaTicket = vendaTicketBd;
            this.selectedVendaTicket = vendaTicketBd;

            selectedTicket = vendaTicketBd.getTicketComprado();

            if (selectedVendaTicket != null && selectedVendaTicket.getId() != null) {
                if (filteredVendaTickets != null && !filteredVendaTickets.isEmpty()) {
                    int index = filteredVendaTickets.indexOf(selectedVendaTicket); //captura o indice do ticket com dados antigo na lista de filtrados
                    filteredVendaTickets.remove(index); //remove o ticket com dados antigo da lista de filtrados
                    filteredVendaTickets.add(index, selectedVendaTicket);  //adiciona o novo ticket com seus novos dados na lista de filtrados              
                }
            }

            if (vendaTicketBd.getContaComprador().getUsuario().getCpf().contains(expressaoBusca)) {
                expressaoBusca = vendaTicketBd.getContaComprador().getUsuario().getCpf();
            } else if (vendaTicketBd.getContaComprador().getUsuario().getNome().contains(expressaoBusca)) {
                expressaoBusca = vendaTicketBd.getContaComprador().getUsuario().getNome();
            } else if (vendaTicketBd.getContaComprador().getUsuario().getMatricula().contains(expressaoBusca)) {
                expressaoBusca = vendaTicketBd.getContaComprador().getUsuario().getMatricula();
            }



            String situacao = (vendaTicketBd.isStatus() == false) ? " \"Não Usado\" " : "\"Usado\"";
            String result = (vendaTicketBd.isStatus() == true) ? " poderá adentrar ao refeitório e fazer um bom apetite da refeição. " : " não poderá entrar no refeitório.";
            String msg = "Status da Venda do ticket comprado para o " + vendaTicketBd.getTicketComprado() + " pelo  usuário: " + vendaTicketBd.getContaComprador().getUsuario().getNome() + " foi alterado para o status de"
                    + situacao
                    + "! Sendo assim o usuário "
                    + result;
            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));




        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }
    }

    public VendaTicketDataModel getVendaTicketDataModel() {

        try {

            List<VendaTicket> lista = ticketDao.listTicketsCompradosPorRefeicaoByExpressaoBusca(selectedTicket.getId(), expressaoBusca);
            vendaTicketDataModel = new VendaTicketDataModel(lista, ticketDao);

            filteredVendaTickets = ticketDao.listTicketsCompradosPorRefeicaoByExpressaoBusca(selectedTicket.getId(), expressaoBusca);

            setQtdTotalTicketsVendidos(ticketDao.getTotalTicketsVendidosByExpressaoBusca(selectedTicket.getId(), expressaoBusca));


        } catch (Exception ex) {
            vendaTicketDataModel = null;
        }





        return vendaTicketDataModel;
    }

    //Implementar Segurança nesses métodos
    public void prepararFormComprarTicket(String loginUserSelecionado) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (loginUserSelecionado != null) {
                selectedUsuario = usuarioDao.getUsuario(loginUserSelecionado);
                //listTickets = ticketDao.listTicketsAVenda();
            } else {
                //lançar exceção selecione o usuário que deseja comprar tickets

                throw new RuntimeException("Selecione o usuário que deseja solicitar/comprar tickets.");
            }
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }

    }


    public void prepararFormComprarTicketUsuarioLogado() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            String loginUserSelecionado = getUsuarioLogado().getUsername();
            if (loginUserSelecionado != null) {
                selectedUsuario = usuarioDao.getUsuario(loginUserSelecionado);                
            } else {
                //lançar exceção selecione o usuário que deseja comprar tickets
                throw new RuntimeException("Selecione o usuário que deseja solicitar/comprar tickets.");
            }
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }
    }


    private Usuario getUsuarioLogado(Session session) {

        Usuario usuarioLogado = new Usuario();

        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                if (authentication.getPrincipal() != null) {
                    String userName = ((User) authentication.getPrincipal()).getUsername();
                    usuarioLogado = usuarioDao.getUsuario(session, userName);
                    usuarioLogado.setUsername(((User) authentication.getPrincipal()).getUsername());
                }
            }
        }
        return usuarioLogado;
    }


    private Usuario getUsuarioLogado() {

        Usuario usuarioLogado = new Usuario();

        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                if (authentication.getPrincipal() != null) {
                    String userName = ((User) authentication.getPrincipal()).getUsername();
                    usuarioLogado = usuarioDao.getUsuario(userName);
                    usuarioLogado.setUsername(((User) authentication.getPrincipal()).getUsername());
                }
            }
        }
        return usuarioLogado;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public List<Ticket> getListTickets() {
        return listTickets;
    }

    public void setListTickets(List<Ticket> listTickets) {
        this.listTickets = listTickets;
    }


    public VendaTicket getVendaTicket() {
        return vendaTicket;
    }

    public void setVendaTicket(VendaTicket vendaTicket) {
        this.vendaTicket = vendaTicket;
    }

    public Ticket getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(Ticket selectedTicket) {
        this.selectedTicket = selectedTicket;
    }

    public List<VendaTicket> getListVendasTickets() {
        return listVendasTickets;
    }

    public void setListVendasTickets(List<VendaTicket> listVendasTickets) {
        this.listVendasTickets = listVendasTickets;
    }

    public Long getQtdTotalTicketsVendidos() {
        return qtdTotalTicketsVendidos;
    }

    public void setQtdTotalTicketsVendidos(Long qtdTotalTicketsVendidos) {
        this.qtdTotalTicketsVendidos = qtdTotalTicketsVendidos;
    }

    public List<Boolean> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<Boolean> listStatus) {
        this.listStatus = listStatus;
    }

    public VendaTicket getSelectedVendaTicket() {
        return selectedVendaTicket;
    }

    public void setSelectedVendaTicket(VendaTicket selectedVendaTicket) {
        this.selectedVendaTicket = selectedVendaTicket;
    }

    public void setVendaTicketDataModel(VendaTicketDataModel vendaTicketDataModel) {
        this.vendaTicketDataModel = vendaTicketDataModel;
    }

    public List<VendaTicket> getFilteredVendaTickets() {
        if (filteredVendaTickets != null && !filteredVendaTickets.isEmpty()) {
            ticketDao.getTotalTicketsVendidos(filteredVendaTickets.get(0).getTicketComprado().getId());
            setQtdTotalTicketsVendidos(qtdTotalTicketsVendidos);
        }
        return filteredVendaTickets;
    }

    public void setFilteredVendaTickets(List<VendaTicket> filteredVendaTickets) {
        this.filteredVendaTickets = filteredVendaTickets;
    }

    public String getExpressaoBusca() {
        return expressaoBusca;
    }

    public void setExpressaoBusca(String expressaoBusca) {
        this.expressaoBusca = expressaoBusca;
    }

    public List<Ticket> getListTicketsDaSemana() {
        return ticketDao.listTicketsCardapioDaSemana();
    }

    public TicketDataModel getListTicketsDaSemanaDataModel() {
        List<Ticket> lista = ticketDao.listTicketsCardapioDaSemana();
        ticketDataModel = new TicketDataModel(lista, ticketDao);
        return ticketDataModel;
    }

    public void setListTicketsDaSemana(List<Ticket> listTicketsDaSemana) {
        this.listTicketsDaSemana = listTicketsDaSemana;
    }

    public String getSemanaCardapio() {

        if (semanaCardapio != null && semanaCardapio.equalsIgnoreCase("")) {
            semanaCardapio = "";
            if (listTicketsDaSemana != null && !listTicketsDaSemana.isEmpty()) {
                semanaCardapio += "(Semana de " + listTickets.get(0).getDataRefeicaoFormatada() + " a "
                        + listTickets.get(listTickets.size() - 1).getDataRefeicaoFormatada() + ")";
            }
        }

        return semanaCardapio;
    }

    public void setSemanaCardapio(String semanaCardapio) {
        this.semanaCardapio = semanaCardapio;
    }

    public String getValoresTicketSemana() {

        if (valoresTicketSemana != null && valoresTicketSemana.equalsIgnoreCase("")) {
            List<TipoUsuario> tipoUsuarioList = tipoUsuarioDao.list();
            valoresTicketSemana = "";
            for (TipoUsuario tipoUsuario : tipoUsuarioList) {
                valoresTicketSemana += tipoUsuario.getDescricao().toUpperCase() + ": " +                                                 
                        Utils.Utils.formataMoeda(tipoUsuario.getValor()) + "  ";
            }
        }

        return valoresTicketSemana;
    }

    public void setValoresTicketSemana(String valoresTicketSemana) {
        this.valoresTicketSemana = valoresTicketSemana;
    }

    public String getSaldoUsuarioLogado() {
        String saldoUsuarioLogado = "";
        try {
            Conta contaUsuarioLogado = null;
            contaUsuarioLogado = contaDao.getContaByUsuario(getUsuarioLogado());
            saldoUsuarioLogado = Utils.Utils.formataMoeda(contaUsuarioLogado.getSaldo().doubleValue());
            //Se o usuario for aluno será exibido crédito ilimitado.
            if(contaUsuarioLogado.getUsuario().getTipoUsuario().getDescricao().equalsIgnoreCase("Aluno")){
              saldoUsuarioLogado = "R$ Ilimitado";  
            }
            
        } catch (Exception ex) {
            saldoUsuarioLogado = "R$ 0,00";
        }
        return saldoUsuarioLogado;
    }

    public Integer quantidadeCompradaPeloUsuarioLogado(Long ticketId) {
        Integer quantidade = 0;
        try {
            quantidade = ticketDao.getQuantidadeCompradaDeTicketsPeloUsuarioPorRefeicao(ticketId, getUsuarioLogado());
        } catch (Exception ex) {
            quantidade = 0;
        }
        return quantidade;
    }


    public void comprarTicketByUsuarioLogado() {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();

        try {

            vendaTicket.setContaComprador(contaDao.getContaByUsuario(session, getUsuarioLogado(session)));
            VendaTicket vendaTicketBd = ticketDao.getVendaTicket(session, vendaTicket);
            session.evict(vendaTicketBd);

            if (vendaTicketBd == null) {
                vendaTicket.setTicketComprado(ticketDao.getTicket(session, vendaTicket.getTicketComprado().getId()));
                vendaTicket.setContaComprador(contaDao.getContaByUsuario(session, getUsuarioLogado(session)));
                vendaTicket.setUsuarioOperador(vendaTicket.getContaComprador().getUsuario());
                vendaTicket.setDataHoraCompra(Calendar.getInstance());
                vendaTicket.setQuantidade(1);
            } else {
                vendaTicket.setId(vendaTicketBd.getId());
                vendaTicket.setQuantidade(vendaTicketBd.getQuantidade());
            }


            vendaTicketValidator.onComprarTicketByUsuarioLogadoInTransaction(session, getUsuarioLogado(session), vendaTicket, getUsuarioLogado(session), usuarioDao, contaDao, ticketDao, tipoOperacaoDao);

            session.update(vendaTicket.getContaComprador());
            session.save(vendaTicket.getRegistroOperacao());

            if (ticketDao.verificaSeVendaTicketJaExisteInTransaction(session, vendaTicket)) {
                session.update(vendaTicket);
            } else {
                session.save(vendaTicket);
            }


            t.commit();

            //limpa os campos e exibe a mensagem de sucesso que a venda do ticket foi concretizada com sucesso!

            String msg = "O Operador: " + vendaTicket.getUsuarioOperador().getNome() + " registrou com sucesso a compra do Ticket: "
                    + vendaTicket.getTicketComprado().toString() + " no valor de " + vendaTicket.getQuantidade().doubleValue()
                    + "X" + vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor() + "=R$ "
                    + (vendaTicket.getQuantidade().doubleValue() * vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor())
                    + " para o usuário(a): \"" + vendaTicket.getContaComprador().getUsuario().getNome() + "\" de CPF: "
                    + vendaTicket.getContaComprador().getUsuario().getCpf() + "! "
                    + " A quantidade total de tickets comprados até o momento para a refeição: "
                    + vendaTicket.getTicketComprado().toString()
                    + " é de " + vendaTicket.getQuantidade().intValue() + " tickets.";
            facesContext.addMessage(null, new FacesMessage(msg));


            vendaTicket = new VendaTicket();

        } catch (Exception e) {
            t.rollback();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        } finally {
            session.close();
        }


    }

    
    public void comprarTicketByUsuarioLogado(Long ticketId) {


        FacesContext facesContext = FacesContext.getCurrentInstance();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();

        try {


            //verificar se o venda ticket ja existe no banco para o usuario logado e para o ticketId informado 
            //se existir incrementar a quantidade comprada mais um e verificar se não ultrapassa o valor de 2
            //verificar também se o ticketId ainda encontra-se disponivel para venda

            //Se a vendaTicket não existir criar uma nova com o ticketId setado e com o usuario logado setado também e incrementar a quantidade
            //verificar se o usuario tem credito suficiente para pagar o ticket;

            vendaTicket.setTicketComprado(ticketDao.getTicket(session, ticketId));
            vendaTicket.setContaComprador(contaDao.getContaByUsuario(session, getUsuarioLogado(session)));


            VendaTicket vendaTicketBd = ticketDao.getVendaTicket(session, vendaTicket);
            session.evict(vendaTicketBd);

            if (vendaTicketBd == null) {
                vendaTicket = new VendaTicket();
                vendaTicket.setTicketComprado(ticketDao.getTicket(session, ticketId));
                vendaTicket.setContaComprador(contaDao.getContaByUsuario(session, getUsuarioLogado(session)));
                vendaTicket.setUsuarioOperador(vendaTicket.getContaComprador().getUsuario());
                vendaTicket.setDataHoraCompra(Calendar.getInstance());
                vendaTicket.setQuantidade(1);
            } else {
                vendaTicket.setId(vendaTicketBd.getId());
                vendaTicket.setQuantidade(vendaTicketBd.getQuantidade());
            }


            vendaTicketValidator.onComprarTicketByUsuarioLogadoInTransaction(session, getUsuarioLogado(session), vendaTicket, getUsuarioLogado(session), usuarioDao, contaDao, ticketDao, tipoOperacaoDao);

            session.update(vendaTicket.getContaComprador());
            session.save(vendaTicket.getRegistroOperacao());

            if (ticketDao.verificaSeVendaTicketJaExisteInTransaction(session, vendaTicket)) {
                session.update(vendaTicket);
            } else {
                session.save(vendaTicket);
            }


            t.commit();

            //limpa os campos e exibe a mensagem de sucesso que a venda do ticket foi concretizada com sucesso!

            String msg = "O Operador: " + vendaTicket.getUsuarioOperador().getNome() + " registrou com sucesso a compra do Ticket: "
                    + vendaTicket.getTicketComprado().toString() + " no valor de " + vendaTicket.getQuantidade().doubleValue()
                    + "X" + vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor() + "=R$ "
                    + (vendaTicket.getQuantidade().doubleValue() * vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor())
                    + " para o usuário(a): \"" + vendaTicket.getContaComprador().getUsuario().getNome() + "\" de CPF: "
                    + vendaTicket.getContaComprador().getUsuario().getCpf() + "! "
                    + " A quantidade total de tickets comprados até o momento para a refeição: "
                    + vendaTicket.getTicketComprado().toString()
                    + " é de " + vendaTicket.getQuantidade().intValue() + " tickets.";
            facesContext.addMessage(null, new FacesMessage(msg));


            vendaTicket = new VendaTicket();

        } catch (Exception e) {
            t.rollback();

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        } finally {
            session.close();
        }



    }


    public void desistirTicketByUsuarioLogado(Long ticketId) {


        FacesContext facesContext = FacesContext.getCurrentInstance();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();

        try {

            vendaTicket.setTicketComprado(ticketDao.getTicket(session, ticketId));
            vendaTicket.setContaComprador(contaDao.getContaByUsuario(session, getUsuarioLogado(session)));


            VendaTicket vendaTicketBd = ticketDao.getVendaTicket(session, vendaTicket);
            
            if(vendaTicketBd==null){
                 throw new RuntimeException("Erro ao desistir do ticket selecionado. Você ainda não comprou nenhum ticket para a refeição selecionada por isso não poderá desistir da mesma.");                
            }
            
            session.evict(vendaTicketBd);

            if (vendaTicketBd != null) {                
                vendaTicket.setId(vendaTicketBd.getId());
                vendaTicket.setQuantidade(vendaTicketBd.getQuantidade());
            }

            vendaTicketValidator.onDesistirTicketByUsuarioLogadoInTransaction(session, getUsuarioLogado(session), vendaTicket, getUsuarioLogado(session), usuarioDao, contaDao, ticketDao, tipoOperacaoDao);

            if (vendaTicket.getQuantidade().intValue() == 0) {
                session.delete(vendaTicket);
            } 

            t.commit();

            //limpa os campos e exibe a mensagem de sucesso que a venda do ticket foi concretizada com sucesso!

            String msg = "O Operador: " + vendaTicket.getUsuarioOperador().getNome() + " registrou com sucesso a desistência do Ticket: "
                    + vendaTicket.getTicketComprado().toString() + " no valor de " + vendaTicket.getQuantidade().doubleValue()
                    + "X" + vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor() + "=R$ "
                    + (vendaTicket.getQuantidade().doubleValue() * vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor())
                    + " para o usuário(a): \"" + vendaTicket.getContaComprador().getUsuario().getNome() + "\" de CPF: "
                    + vendaTicket.getContaComprador().getUsuario().getCpf() + "! "
                    + " A quantidade total de tickets comprados até o momento para a refeição: "
                    + vendaTicket.getTicketComprado().toString()
                    + " é de " + vendaTicket.getQuantidade().intValue() + " tickets.";
            facesContext.addMessage(null, new FacesMessage(msg));


            vendaTicket = new VendaTicket();

        } catch (Exception e) {
            t.rollback();

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        } finally {
            session.close();
        }



    }
}
