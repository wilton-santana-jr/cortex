/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Install;

import Model.TipoUsuario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Wilton
 */
public class TipoUsuarioInstall {

    public TipoUsuarioInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarTipoUsuarios(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }

    private void instalarTipoUsuarios(Session session) throws HibernateException {
        System.out.println(" \n --- Início da  Instalação de tipos de Usuários --- \n ");

        session.save(new TipoUsuario(new Long(1), "Aluno", 0.0));
        session.save(new TipoUsuario(new Long(2), "Professor", 2.0));
        session.save(new TipoUsuario(new Long(3), "Técnico Administrativo", 2.0));
        session.save(new TipoUsuario(new Long(4), "Terceirizado", 1.0));

        System.out.println(" \n --- Fim da  Instalação de tipos de Usuários --- \n ");
    }
}
