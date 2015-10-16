/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.validator;

import Interfaces.dao.IRepositorioTickets;
import Interfaces.dao.IRepositorioTipoRefeicoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Ticket;
import java.io.Serializable;

/**
 *
 * @author Consultorio
 */
public interface ITicketValidator extends Serializable {

    void onCreateTicket(Ticket ticket, IRepositorioTickets daoTicket, IRepositorioTipoRefeicoes daoTipoRefeicao, IRepositorioUsuarios daoUser);

    void onUpdateTicket(Ticket ticket, IRepositorioTickets daoTicket, IRepositorioTipoRefeicoes daoTipoRefeicao, IRepositorioUsuarios daoUser);
    
}
