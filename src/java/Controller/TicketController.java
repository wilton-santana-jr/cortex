package Controller;

import Dao.RepositorioTicketsBDR;
import Dao.RepositorioTipoRefeicoesBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioTickets;
import Interfaces.dao.IRepositorioTipoRefeicoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.RegistroOperacao;
import Model.Ticket;
import Model.TipoRefeicao;
import Model.Usuario;
import Validator.TicketValidator;
import Interfaces.validator.ITicketValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import selectble.TicketDataModel;

@ManagedBean
@ViewScoped
public class TicketController implements Serializable {

    private Ticket ticketEdit;//usado para adicionar ou remover o ou altera usuario
    private Ticket selectedTicket;
    private List<TipoRefeicao> tipoRefeicaoList;
    private List<RegistroOperacao> listRegistroOperacao;
    private Date dataFormIni;
    private Date dataFormFim;
    private Date dataFormIniMin;
    private Date dataFormFimMax;
    private IRepositorioTickets ticketDao;
    private IRepositorioTipoRefeicoes tipoRefeicaoDao;
    private IRepositorioUsuarios usuarioDao;
    private TicketDataModel ticketDataModel;
    private List<Ticket> filteredTickets;
    private ITicketValidator ticketValidator;

    public TicketController() {


        Calendar dataInicioMin = Calendar.getInstance();
        dataInicioMin.add(Calendar.MONTH, -3);
        Calendar dataFimMin = Calendar.getInstance();

        dataFormIniMin = dataInicioMin.getTime();
        dataFormFimMax = dataFimMin.getTime();

        dataFormIni = Calendar.getInstance().getTime();
        dataFormFim = Calendar.getInstance().getTime();

        ticketEdit = new Ticket();
        selectedTicket = new Ticket();

        tipoRefeicaoList = new ArrayList<>();
        listRegistroOperacao = new ArrayList<>();


        tipoRefeicaoDao = new RepositorioTipoRefeicoesBDR();
        ticketDao = new RepositorioTicketsBDR();
        usuarioDao = new RepositorioUsuariosBDR();

        ticketValidator = new TicketValidator();

        tipoRefeicaoList = tipoRefeicaoDao.list();


    }

    public void handleTipoRefeicaoChange() {

        //Se for selecionado almoço e 
        //horario inicial e horario final estiverem vazios preencher com o valor padrão

        try {
            if (ticketEdit.getTipoRefeicao() != null && ticketEdit.getTipoRefeicao().getId() != null) {
                TipoRefeicao tipoRefeicaoSelecionada = tipoRefeicaoDao.getTipoRefeicao(ticketEdit.getTipoRefeicao().getId());
                ticketEdit.setTipoRefeicao(tipoRefeicaoSelecionada);

                if (ticketEdit.getDataRefeicao() != null) {
                    ticketEdit.setHoraInicioRefeicao(ticketEdit.getDataRefeicao());
                    ticketEdit.setHoraFimRefeicao(ticketEdit.getDataRefeicao());
                } else {
                    ticketEdit.setHoraInicioRefeicao(Calendar.getInstance().getTime());
                    ticketEdit.setHoraFimRefeicao(Calendar.getInstance().getTime());
                }

                //Se for selecionado almoço e 
                //horario inicial e horario final estiverem vazios preencher com o valor padrão
                if (ticketEdit.getTipoRefeicao() != null && ticketEdit.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("A")) {



                    ticketEdit.getHoraInicioRefeicao().setHours(11);
                    ticketEdit.getHoraInicioRefeicao().setMinutes(30);
                    ticketEdit.getHoraInicioRefeicao().setSeconds(0);

                    ticketEdit.getHoraFimRefeicao().setHours(13);
                    ticketEdit.getHoraFimRefeicao().setMinutes(30);
                    ticketEdit.getHoraFimRefeicao().setSeconds(0);

                }

                //Se for selecionado Jantar e 
                //horario inicial e horario final estiverem vazios preencher com o valor padrão
                if (ticketEdit.getTipoRefeicao() != null && ticketEdit.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("J")) {

                    ticketEdit.getHoraInicioRefeicao().setHours(17);
                    ticketEdit.getHoraInicioRefeicao().setMinutes(30);
                    ticketEdit.getHoraInicioRefeicao().setSeconds(0);

                    ticketEdit.getHoraFimRefeicao().setHours(19);
                    ticketEdit.getHoraFimRefeicao().setMinutes(0);
                    ticketEdit.getHoraFimRefeicao().setSeconds(0);


                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Ticket> getFilteredTickets() {
        return filteredTickets;
    }

    public void setFilteredTickets(List<Ticket> filteredTickets) {
        this.filteredTickets = filteredTickets;
    }

    public Ticket getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(Ticket selectedTicket) {
        this.selectedTicket = selectedTicket;
    }

    public Ticket getTicketEdit() {
        return ticketEdit;
    }

    public void setTicketEdit(Ticket ticketEdit) {
        this.ticketEdit = ticketEdit;
    }

    public List<TipoRefeicao> getTipoRefeicaoList() {
        return tipoRefeicaoList;
    }

    public void setTipoRefeicaoList(List<TipoRefeicao> tipoRefeicaoList) {
        this.tipoRefeicaoList = tipoRefeicaoList;
    }

    public TicketDataModel getTicketDataModel() {
        List<Ticket> lista = ticketDao.list();
        ticketDataModel = new TicketDataModel(lista, ticketDao);
        return ticketDataModel;
    }

    public void prepararAdicionarTicket() {
        ticketEdit = new Ticket();
        tipoRefeicaoList = tipoRefeicaoDao.list();
    }

    public void prepararRelatorioBoletoCreditado() {

        listRegistroOperacao = new ArrayList<>();

    }

    public void prepararAlterarTicket(String idTicket) {
        ticketEdit = ticketDao.getTicket(new Long(idTicket));
        tipoRefeicaoList = tipoRefeicaoDao.list();
    }

    //evento chamado quando seleciona uma linha da tabela de tickets
    public void onRowSelect(SelectEvent event) {

        Ticket ticket = (Ticket) event.getObject();

        try {
            tipoRefeicaoList = tipoRefeicaoDao.list();
        } catch (Exception ex) {
            tipoRefeicaoList = new ArrayList<>();
        }

        this.selectedTicket = ticket;

    }

    public void adicionarTicket() {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {

            ticketEdit.setUsuarioOperador(getUsuarioLogado());
            ticketValidator.onCreateTicket(ticketEdit, ticketDao, tipoRefeicaoDao, usuarioDao);

            ticketDao.save(ticketEdit);
            String msg = "Dados do  ticket: " + ticketEdit.getTipoRefeicao().getNome() + " do dia: " + ticketEdit.getDataRefeicaoToString() + " adicionado com sucesso! ";
            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));
            this.selectedTicket = ticketEdit;

            this.prepararAdicionarTicket();


            if (filteredTickets != null) {
                filteredTickets.add(ticketDataModel.getRowIndex(), selectedTicket);
            }

        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }

    }

    public void alterarTicket() {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {

            selectedTicket.setUsuarioOperador(getUsuarioLogado());

            ticketValidator.onUpdateTicket(selectedTicket, ticketDao, tipoRefeicaoDao, usuarioDao);

            ticketDao.update(selectedTicket);//ticketEdit            
            String msg = "Dados do  ticket: " + selectedTicket.getTipoRefeicao().getNome() + " do dia: " + selectedTicket.getDataRefeicaoToString() + " alterados com sucesso! ";
            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));

            if (filteredTickets != null) {
                int index = filteredTickets.indexOf(selectedTicket); //captura o indice do ticket com dados antigo na lista de filtrados
                filteredTickets.remove(index); //remove o ticket com dados antigo da lista de filtrados
                filteredTickets.add(index, selectedTicket);  //adiciona o novo ticket com seus novos dados na lista de filtrados              
            }

        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }
    }

    /**
     * Método usado para validar campos com espaços em branco.
     *
     * @param contexto
     * @param componente
     * @param valor
     */
    public void validarEspacoBranco(FacesContext contexto, UIComponent componente, Object valor) {
        Long valorLong = (Long) valor;
        if (valorLong != null && valorLong.longValue() == 0) {
            ((UIInput) componente).setValid(false);
            String mensagem = componente.getAttributes().get("label") + ": Valor inválido, selecione um ticket válido para edição.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, mensagem);
            contexto.addMessage(componente.getClientId(contexto), facesMessage);
        }
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

    public List<RegistroOperacao> getListRegistroOperacao() {
        return listRegistroOperacao;
    }

    public void setListRegistroOperacao(List<RegistroOperacao> listRegistroOperacao) {
        this.listRegistroOperacao = listRegistroOperacao;
    }

    public Date getDataFormIni() {
        return dataFormIni;
    }

    public void setDataFormIni(Date dataFormIni) {
        this.dataFormIni = dataFormIni;
    }

    public Date getDataFormFim() {
        return dataFormFim;
    }

    public void setDataFormFim(Date dataFormFim) {
        this.dataFormFim = dataFormFim;
    }

    public Date getDataFormIniMin() {
        return dataFormIniMin;
    }

    public void setDataFormIniMin(Date dataFormIniMin) {
        this.dataFormIniMin = dataFormIniMin;
    }

    public Date getDataFormFimMax() {
        return dataFormFimMax;
    }

    public void setDataFormFimMax(Date dataFormFimMax) {
        this.dataFormFimMax = dataFormFimMax;
    }

    public List<RegistroOperacao> exibirRelatorioBoletoCreditado() {


        try {                                   
            listRegistroOperacao = ticketDao.relatorioBoletosCreditadosContaDosUsuarios(dataFormIni, dataFormFim);            
        } catch (Exception ex) {
            listRegistroOperacao = null;
        }

        return listRegistroOperacao;
    }
}
