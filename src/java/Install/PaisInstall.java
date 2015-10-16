/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Install;

import Model.Pais;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PaisInstall {

    public PaisInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarPaises(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }

    private void instalarPaises(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de países ---- \n");

        session.save(new Pais(new Long(1), "Brasil", "BR"));

        System.out.println(" \n --- Fim da  Instalação de países --- \n ");
    }
}
