package Install;

import Model.Conta;
import java.math.BigDecimal;
import java.util.Calendar;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ContaInstall {
    
     public ContaInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarContas(session);
            t.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }
     
      private void instalarContas(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de contas dos usuarios ---- \n");
        
        Calendar dataCriacao = Calendar.getInstance();
        BigDecimal saldo = new BigDecimal(0);
        
        session.save(new Conta(new Long(1),"admin", saldo, dataCriacao));
        session.save(new Conta(new Long(2),"lojinha", saldo, dataCriacao));        
        session.save(new Conta(new Long(3),"professor", saldo, dataCriacao));
               

        System.out.println(" \n --- Fim da  Instalação de contas dos usuarios --- \n ");
    }
    
    
}
