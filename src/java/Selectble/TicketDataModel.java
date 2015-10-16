package selectble;

import Interfaces.dao.IRepositorioTickets;
import Model.Ticket;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

public class TicketDataModel extends ListDataModel<Ticket> implements Serializable, SelectableDataModel<Ticket> {

    private IRepositorioTickets ticketDao;

    public TicketDataModel() {
    }

    public TicketDataModel(List<Ticket> data) {
        super(data);
    }
    
    public TicketDataModel(List<Ticket> data,IRepositorioTickets ticketDao) {
        super(data);
        this.ticketDao = ticketDao;
    }

    @Override
    public Ticket getRowData(String rowKey) {
        try{
            Ticket ticket = ticketDao.getTicket(new Long(rowKey));
            return ticket;                         
        }catch(Exception e){
            return null;
        }    
    }

    @Override
    public Object getRowKey(Ticket ticket) 
    {
                 return ticket.getId();
    }  
    
    
    
    
}
