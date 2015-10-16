
package Install;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class VendaTicketInstall {

    public VendaTicketInstall(org.hibernate.Session session) {
//        Transaction t = session.beginTransaction();
//        try {
//            t.begin();
//            instalarVendaTickets(session);
//            t.commit();
//        } catch (Exception ex) {
//            t.rollback();
//            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
//        } finally {
//            session.close();
//        }

    }
    
    
    
    private void instalarVendaTickets(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de vendas de tickets ---- \n");      

        System.out.println(" \n --- Fim da  Instalação de vendas de tickets --- \n ");
    }
    
}
