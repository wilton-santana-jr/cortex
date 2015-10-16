/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.dao;

import Model.RegistroOperacao;
import Model.Ticket;
import Model.TipoRefeicao;
import Model.Usuario;
import Model.VendaTicket;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Consultorio
 */
public interface IRepositorioTickets extends Serializable {

    //verifica se o ticketComprado na vendaTicket encontra-se disponível ainda para venda
    boolean estaNoPeriodoDeVenda(VendaTicket vendaTicket);

    //verifica se o ticketComprado na vendaTicket encontra-se disponível ainda para venda
    boolean estaNoPeriodoDeVenda(Session session, VendaTicket vendaTicket);

    boolean existeNoBanco(Date dataRefeicao, TipoRefeicao tipoRefeicao);

    Integer getQuantidadeCompradaDeTicketsPeloUsuarioPorRefeicao(Long ticketId, Usuario usuarioLogado);

    Ticket getTicket(Long idTicket);

    Ticket getTicket(Session session, Long idTicket);

    Ticket getTicketByTipoAndDataRefeicao(TipoRefeicao tipoRefeicao, Date dataRefeicao);

    List<Ticket> getTicketsByDataRefeicao(Date dataRefeicao);

    //retornar o total de tickets vendidos para um determinado ticket selecionado
    Long getTotalTicketsVendidos(Long ticketId);

    Long getTotalTicketsVendidosByExpressaoBusca(Long ticketId, String expressaoBusca);

    VendaTicket getVendaTicket(Long idVendaTicket);

    //Verifica se foi feita uma determinada venda de um determinado ticket para um determinado usuario
    VendaTicket getVendaTicket(VendaTicket vendaTicket);

    //Verifica se foi feita uma determinada venda de um determinado ticket para um determinado usuario
    VendaTicket getVendaTicket(Session session, VendaTicket vendaTicket);

    List<Ticket> list();

    /**
     * Lista os tickets que ainda encontram-se disponíveis a venda
     *
     * @return
     */
    List<Ticket> listTicketsAVenda();

    List<Ticket> listTicketsCardapioDaSemana();

    //lista os tickets comprados para uma determinada refeição selecionada
    List<VendaTicket> listTicketsCompradosPorRefeicao(Long ticketId);

    List<VendaTicket> listTicketsCompradosPorRefeicaoByExpressaoBusca(Long ticketId, String expressaoBusca);

    List<RegistroOperacao> relatorioBoletosCreditadosContaDosUsuarios(Date dataFormIni, Date dataFormFim);

    void save(Ticket ticket);

    void update(Ticket ticket);

    void update(VendaTicket vendaTicketBd);

    boolean verificaSeAlguemJaComprouTicket(Ticket ticket);

    //Verificar se a horacorrente esta entre o (horario inicial da refeicao-10 minutes) e
    //o horario final da refeicao
    //se não estiver informar que o tempo para utilização do ticket se esgotou e o mesmo não poderá mais ser utilizado
    //você deveria ter utilizado o mesmo no período de horário da refeição
    boolean verificaSeVendaTicketEstaAptoAEntrarNoRefeitorioParaComer(VendaTicket vendaTicket);

    boolean verificaSeVendaTicketJaExiste(VendaTicket vendaTicket);

    boolean verificaSeVendaTicketJaExisteInTransaction(Session session, VendaTicket vendaTicket);
    
}
