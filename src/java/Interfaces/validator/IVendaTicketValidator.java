/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.validator;

import Dao.RepositorioContasBDR;
import Dao.RepositorioTicketsBDR;
import Dao.RepositorioTipoOperacoesBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioContas;
import Interfaces.dao.IRepositorioTickets;
import Interfaces.dao.IRepositorioTipoOperacoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Usuario;
import Model.VendaTicket;
import java.io.Serializable;
import org.hibernate.Session;

/**
 *
 * @author Consultorio
 */
public interface IVendaTicketValidator extends Serializable {

    void onComprarTicketByUsuarioInTransaction(Session session, Usuario selectedUsuario, VendaTicket vendaTicket, Usuario usuarioOperador, RepositorioUsuariosBDR usuarioDao, RepositorioContasBDR contaDao, RepositorioTicketsBDR ticketDao, RepositorioTipoOperacoesBDR tipoOperacaoDao);

    //Verificar se o usuario logado esta comprando ticket para ele mesmo
    //verificar ainda se o ticket comprado é um ticket válido
    //ou seja se ele e encontra no momento da compra disponivel para venda
    //se não estiver disponivel para venda lançar uma exceção
    void onComprarTicketByUsuarioLogadoInTransaction(Session session, Usuario selectedUsuario, VendaTicket vendaTicket, Usuario usuarioOperador, IRepositorioUsuarios usuarioDao, IRepositorioContas contaDao, IRepositorioTickets ticketDao, IRepositorioTipoOperacoes tipoOperacaoDao);

    //Verificar se o usuario logado esta desistindo de um ticket para ele mesmo
    //verificar ainda se o ticket desistido é um ticket válido
    //ou seja se ele se encontra no momento da desistencia disponivel para desistencia
    //se não estiver disponivel para desistencia lançar uma exceção
    void onDesistirTicketByUsuarioLogadoInTransaction(Session session, Usuario selectedUsuario, VendaTicket vendaTicket, Usuario usuarioOperador, IRepositorioUsuarios usuarioDao, IRepositorioContas contaDao, IRepositorioTickets ticketDao, IRepositorioTipoOperacoes tipoOperacaoDao);

    void onUpdateStatusVendaTicket(VendaTicket vendaTicket, IRepositorioTickets ticketDao);
    
}
