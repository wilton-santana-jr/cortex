package Install;

import Model.TipoRefeicao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class TipoRefeicaoInstall {

    public TipoRefeicaoInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarTiposRefeicoes(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }

    private void instalarTiposRefeicoes(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de tipos de refeições ---- \n");

        session.save(new TipoRefeicao(new Long(1), "Almoço", "A"));
        session.save(new TipoRefeicao(new Long(2), "Jantar", "J"));

        System.out.println(" \n --- Fim da  Instalação de tipos de refeições --- \n ");
    }
}
