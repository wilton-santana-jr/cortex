package Install;

import Model.Ticket;
import Model.TipoRefeicao;
import Model.Usuario;
import java.util.Calendar;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TicketInstall {

    public TicketInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarTickets(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }

    private void instalarTickets(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de tickets ---- \n");

        Ticket ticketAlmocoOntem = ticketAlmocoOntem();
        ticketAlmocoOntem.setCardapioRefeicao("Picanha, arroz, batata frita e feijão.");
        
        Ticket ticketJantarOntem = ticketJantarOntem();
        ticketJantarOntem.setCardapioRefeicao("Feijoada, arroz, salada e suco.");
        
        Ticket ticketAlmocoHoje = ticketAlmocoHoje();
         ticketAlmocoHoje.setCardapioRefeicao("Cuscus, torta de frango, misto, lasanha");
        
        Ticket ticketJantarHoje = ticketJantarHoje();
         ticketJantarHoje.setCardapioRefeicao("lasanha, sopa.");

        Ticket ticketAlmocoAmanha = ticketAlmocoAmanha();
         ticketAlmocoAmanha.setCardapioRefeicao("Panelada, sarapatel, bode.");

        Ticket ticketJantarAmanha = ticketJantarAmanha();
         ticketJantarAmanha.setCardapioRefeicao("Figado acebolado, arroz branco e fava.");

        Ticket ticketAlmocoDepoisDeAmanha = ticketAlmocoDepoisDeAmanha();
         ticketAlmocoDepoisDeAmanha.setCardapioRefeicao("Pizza de Camarão, sushi.");

        Ticket ticketJantarDepoisDeAmanha = ticketJantarDepoisDeAmanha();
         ticketJantarDepoisDeAmanha.setCardapioRefeicao("Arroz preto com caldo de galinha.");




        session.save(ticketAlmocoOntem);
        session.save(ticketJantarOntem);
        session.save(ticketAlmocoHoje);
        session.save(ticketJantarHoje);
        session.save(ticketAlmocoAmanha);
        session.save(ticketJantarAmanha);
        session.save(ticketAlmocoDepoisDeAmanha);
        session.save(ticketJantarDepoisDeAmanha);



        System.out.println(" \n --- Fim da  Instalação de tickets --- \n ");
    }

    ////////////////////////////
    
    
    private Ticket ticketJantarDepoisDeAmanha() {
        
        Calendar dataJantarDepoisDeAmanha = Calendar.getInstance();
        Calendar horarioInicioJantarDepoisDeAmanha =  Calendar.getInstance();
        Calendar horarioFimJantarDepoisDeAmanha =  Calendar.getInstance();
        
        dataJantarDepoisDeAmanha.add(Calendar.DAY_OF_MONTH, +2);
        
        horarioInicioJantarDepoisDeAmanha.add(Calendar.DAY_OF_MONTH, +2);
        horarioInicioJantarDepoisDeAmanha.set(Calendar.HOUR_OF_DAY, 17);
        horarioInicioJantarDepoisDeAmanha.set(Calendar.MINUTE, 30);
        horarioInicioJantarDepoisDeAmanha.set(Calendar.SECOND, 0);
        horarioInicioJantarDepoisDeAmanha.set(Calendar.MILLISECOND, 0);
        
        horarioFimJantarDepoisDeAmanha.add(Calendar.DAY_OF_MONTH, +2);
        horarioFimJantarDepoisDeAmanha.set(Calendar.HOUR_OF_DAY, 19);
        horarioFimJantarDepoisDeAmanha.set(Calendar.MINUTE, 30);
        horarioFimJantarDepoisDeAmanha.set(Calendar.SECOND, 0);
        horarioFimJantarDepoisDeAmanha.set(Calendar.MILLISECOND, 0);
        Ticket ticketJantarDepoisDeAmanha = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(2)),
                dataJantarDepoisDeAmanha.getTime(), horarioInicioJantarDepoisDeAmanha.getTime(), horarioFimJantarDepoisDeAmanha.getTime());
        
        return ticketJantarDepoisDeAmanha;
    }

    private Ticket ticketAlmocoDepoisDeAmanha() {
        
        Calendar dataAlmocoDepoisDeAmanha = Calendar.getInstance();
        Calendar horarioInicioAlmocoDepoisDeAmanha = Calendar.getInstance();
        Calendar horarioFimAlmocoDepoisDeAmanha = Calendar.getInstance();
        
        dataAlmocoDepoisDeAmanha.add(Calendar.DAY_OF_MONTH, +2);
        
        horarioInicioAlmocoDepoisDeAmanha.add(Calendar.DAY_OF_MONTH, +2);
        horarioInicioAlmocoDepoisDeAmanha.set(Calendar.HOUR_OF_DAY, 11);
        horarioInicioAlmocoDepoisDeAmanha.set(Calendar.MINUTE, 30);
        horarioInicioAlmocoDepoisDeAmanha.set(Calendar.SECOND, 0);
        horarioInicioAlmocoDepoisDeAmanha.set(Calendar.MILLISECOND, 0);
        
        horarioFimAlmocoDepoisDeAmanha.add(Calendar.DAY_OF_MONTH, +2);
        horarioFimAlmocoDepoisDeAmanha.set(Calendar.HOUR_OF_DAY, 13);
        horarioFimAlmocoDepoisDeAmanha.set(Calendar.MINUTE, 30);
        horarioFimAlmocoDepoisDeAmanha.set(Calendar.SECOND, 0);
        horarioFimAlmocoDepoisDeAmanha.set(Calendar.MILLISECOND, 0);
        
        Ticket ticketAlmocoDepoisDeAmanha = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(1)),
                dataAlmocoDepoisDeAmanha.getTime(), horarioInicioAlmocoDepoisDeAmanha.getTime(), horarioFimAlmocoDepoisDeAmanha.getTime());
        
        return ticketAlmocoDepoisDeAmanha;
    }

    private Ticket ticketJantarAmanha() {
        Calendar dataJantarAmanha = Calendar.getInstance();
        Calendar horarioInicioJantarAmanha = Calendar.getInstance();
        Calendar horarioFimJantarAmanha = Calendar.getInstance();
        
        dataJantarAmanha.add(Calendar.DAY_OF_MONTH, +1);
        
        horarioInicioJantarAmanha.add(Calendar.DAY_OF_MONTH, +1);
        horarioInicioJantarAmanha.set(Calendar.HOUR_OF_DAY, 17);
        horarioInicioJantarAmanha.set(Calendar.MINUTE, 30);
        horarioInicioJantarAmanha.set(Calendar.SECOND, 0);
        horarioInicioJantarAmanha.set(Calendar.MILLISECOND, 0);
        
        horarioFimJantarAmanha.add(Calendar.DAY_OF_MONTH, +1);
        horarioFimJantarAmanha.set(Calendar.HOUR_OF_DAY, 19);
        horarioFimJantarAmanha.set(Calendar.MINUTE, 30);
        horarioFimJantarAmanha.set(Calendar.SECOND, 0);
        horarioFimJantarAmanha.set(Calendar.MILLISECOND, 0);
        
        Ticket ticketJantarAmanha = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(2)),
                dataJantarAmanha.getTime(), horarioInicioJantarAmanha.getTime(), horarioFimJantarAmanha.getTime());
        
        return ticketJantarAmanha;
    }

    private Ticket ticketAlmocoAmanha() {
        Calendar dataAlmocoAmanha = Calendar.getInstance();
        Calendar horarioInicioAlmocoAmanha = Calendar.getInstance();
        Calendar horarioFimAlmocoAmanha = Calendar.getInstance();
        
        dataAlmocoAmanha.add(Calendar.DAY_OF_MONTH, +1);
                
        horarioInicioAlmocoAmanha.add(Calendar.DAY_OF_MONTH, +1);
        horarioInicioAlmocoAmanha.set(Calendar.HOUR_OF_DAY, 11);
        horarioInicioAlmocoAmanha.set(Calendar.MINUTE, 30);
        horarioInicioAlmocoAmanha.set(Calendar.SECOND, 0);
        horarioInicioAlmocoAmanha.set(Calendar.MILLISECOND, 0);
        
        horarioFimAlmocoAmanha.add(Calendar.DAY_OF_MONTH, +1);
        horarioFimAlmocoAmanha.set(Calendar.HOUR_OF_DAY, 13);
        horarioFimAlmocoAmanha.set(Calendar.MINUTE, 30);
        horarioFimAlmocoAmanha.set(Calendar.SECOND, 0);
        horarioFimAlmocoAmanha.set(Calendar.MILLISECOND, 0);
        
        Ticket ticketAlmocoAmanha = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(1)),
                dataAlmocoAmanha.getTime(), horarioInicioAlmocoAmanha.getTime(), horarioFimAlmocoAmanha.getTime());
        
        return ticketAlmocoAmanha;
    }

    private Ticket ticketJantarHoje() {
        Calendar dataJantarHoje = Calendar.getInstance();
        Calendar horarioInicioJantarHoje = Calendar.getInstance();
        Calendar horarioFimJantarHoje = Calendar.getInstance();
        
       
        horarioInicioJantarHoje.set(Calendar.HOUR_OF_DAY, 17);
        horarioInicioJantarHoje.set(Calendar.MINUTE, 30);
        horarioInicioJantarHoje.set(Calendar.SECOND, 0);
        horarioInicioJantarHoje.set(Calendar.MILLISECOND, 0);
        
        horarioFimJantarHoje.set(Calendar.HOUR_OF_DAY, 19);
        horarioFimJantarHoje.set(Calendar.MINUTE, 30);
        horarioFimJantarHoje.set(Calendar.SECOND, 0);
        horarioFimJantarHoje.set(Calendar.MILLISECOND, 0);
        
        Ticket ticketJantarHoje = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(2)),
                dataJantarHoje.getTime(), horarioInicioJantarHoje.getTime(), horarioFimJantarHoje.getTime());
        
        return ticketJantarHoje;
    }

    private Ticket ticketAlmocoHoje(){
            
        Calendar dataAlmocoHoje = Calendar.getInstance();
        
        Calendar horarioInicioAlmocoHoje = Calendar.getInstance();       
        Calendar horarioFimAlmocoHoje = Calendar.getInstance();
        
        horarioInicioAlmocoHoje.set(Calendar.HOUR_OF_DAY, 11);
        horarioInicioAlmocoHoje.set(Calendar.MINUTE, 30);
        horarioInicioAlmocoHoje.set(Calendar.SECOND, 0);
        horarioInicioAlmocoHoje.set(Calendar.MILLISECOND, 0);
        
        horarioFimAlmocoHoje.set(Calendar.HOUR_OF_DAY, 13);
        horarioFimAlmocoHoje.set(Calendar.MINUTE, 30);
        horarioFimAlmocoHoje.set(Calendar.SECOND, 0);
        horarioFimAlmocoHoje.set(Calendar.MILLISECOND, 0);
        
        Ticket ticketAlmocoHoje = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(1)),
                dataAlmocoHoje.getTime(), horarioInicioAlmocoHoje.getTime(), horarioFimAlmocoHoje.getTime());
        
        return ticketAlmocoHoje;
    }

    private Ticket ticketJantarOntem() {
        
        Calendar dataJantarOntem = Calendar.getInstance();
        dataJantarOntem.add(Calendar.DAY_OF_MONTH, -1);
        
        Calendar horarioInicioJantarOntem = Calendar.getInstance();
        Calendar horarioFimJantarOntem = Calendar.getInstance();
        
        horarioInicioJantarOntem.add(Calendar.DAY_OF_MONTH, -1);
        horarioInicioJantarOntem.set(Calendar.HOUR_OF_DAY, 17);
        horarioInicioJantarOntem.set(Calendar.MINUTE, 30);
        horarioInicioJantarOntem.set(Calendar.SECOND, 0);
        horarioInicioJantarOntem.set(Calendar.MILLISECOND, 0);
        
        horarioFimJantarOntem.add(Calendar.DAY_OF_MONTH, -1);
        horarioFimJantarOntem.set(Calendar.HOUR_OF_DAY, 19);
        horarioFimJantarOntem.set(Calendar.MINUTE, 30);
        horarioFimJantarOntem.set(Calendar.SECOND, 0);
        horarioFimJantarOntem.set(Calendar.MILLISECOND, 0);
        
        Ticket ticketJantarOntem = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(2)),
                dataJantarOntem.getTime(), horarioInicioJantarOntem.getTime(), horarioFimJantarOntem.getTime());
        
        return ticketJantarOntem;
    }

    private Ticket ticketAlmocoOntem() {
        
        Calendar dataAlmocoOntem = Calendar.getInstance();
        dataAlmocoOntem.add(Calendar.DAY_OF_MONTH, -1);
        
        Calendar horarioInicioAlmocoOntem = Calendar.getInstance();
        Calendar horarioFimAlmocoOntem = Calendar.getInstance();
        
        horarioInicioAlmocoOntem.add(Calendar.DAY_OF_MONTH, -1);
        horarioInicioAlmocoOntem.set(Calendar.HOUR_OF_DAY, 11);
        horarioInicioAlmocoOntem.set(Calendar.MINUTE, 30);
        horarioInicioAlmocoOntem.set(Calendar.SECOND, 0);
        horarioInicioAlmocoOntem.set(Calendar.MILLISECOND, 0);
        
        horarioFimAlmocoOntem.add(Calendar.DAY_OF_MONTH, -1);
        horarioFimAlmocoOntem.set(Calendar.HOUR_OF_DAY, 13);
        horarioFimAlmocoOntem.set(Calendar.MINUTE, 30);
        horarioFimAlmocoOntem.set(Calendar.SECOND, 0);
        horarioFimAlmocoOntem.set(Calendar.MILLISECOND, 0);
        
        Ticket ticketAlmocoOntem = new Ticket(new Usuario("ticket"), new TipoRefeicao(new Long(1)),
                dataAlmocoOntem.getTime(), horarioInicioAlmocoOntem.getTime(), horarioFimAlmocoOntem.getTime());
        
        return ticketAlmocoOntem;
    }
}