package Install;

import Model.Autorizacao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AutorizacaoInstall {

    public AutorizacaoInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarAutorizacoes(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }

    private void instalarAutorizacoes(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de autorizações ---- \n");

        session.save(new Autorizacao("ROLE_TICKET", "Administração de Tickets"));
        session.save(new Autorizacao("ROLE_ADM", "Administração de Usuários"));
        session.save(new Autorizacao("ROLE_LOJINHA", "Administração da Lojinha"));
        session.save(new Autorizacao("ROLE_BOLETO", "Geração de Boletos Bancários"));
        session.save(new Autorizacao("ROLE_COMUM", "Usuário Limitado"));
        session.save(new Autorizacao("ROLE_ALUNO", "Usuário Aluno"));

        System.out.println(" \n --- Fim da  Instalação de autorizações --- \n ");
    }
}
