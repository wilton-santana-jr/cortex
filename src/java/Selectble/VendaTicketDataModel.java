package selectble;

import Interfaces.dao.IRepositorioTickets;
import Model.VendaTicket;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

public class VendaTicketDataModel extends ListDataModel<VendaTicket> implements Serializable, SelectableDataModel<VendaTicket> {

    private IRepositorioTickets ticketDao;

    public VendaTicketDataModel() {
    }

    public VendaTicketDataModel(List<VendaTicket> data) {
        super(data);
    }
    
    public VendaTicketDataModel(List<VendaTicket> data,IRepositorioTickets ticketDao) {
        super(data);
        this.ticketDao = ticketDao;
    }

    @Override
    public VendaTicket getRowData(String rowKey) {
           try{
            VendaTicket vendaTicket;
               vendaTicket = ticketDao.getVendaTicket(new Long(rowKey));
            return vendaTicket;                         
        }catch(Exception e){
            return null;
        }                            
    }

    @Override
    public Object getRowKey(VendaTicket vendaTicket) {
        return vendaTicket.getId();
    }  
    
}