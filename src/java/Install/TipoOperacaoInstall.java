package Install;

import Model.TipoOperacao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TipoOperacaoInstall {
    
    public TipoOperacaoInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarTiposDeOperacoes(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }
    
    private void instalarTiposDeOperacoes(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de tipos de Operações ---- \n");

        session.save(new TipoOperacao(new Long(1),"Crédito", "C"));
        session.save(new TipoOperacao(new Long(2),"Débito", "D"));

        System.out.println(" \n --- Fim da  Instalação de tipos de Operações --- \n ");
    }
    
}
