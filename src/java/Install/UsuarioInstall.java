package Install;

import Model.Autorizacao;
import Model.Cidade;
import Model.TipoUsuario;
import Model.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UsuarioInstall {

    public UsuarioInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarUsuarios(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }
    }

    private void instalarUsuarios(Session session) throws HibernateException {
        System.out.println(" \n --- Início da  Instalação de usuários --- \n ");        
        inserirUsuarioAdmin(session);        
        inserirUsuarioLojinha(session);        
        inserirUsuarioProfessor(session);    
        inserirUsuarioAluno(session);    
        inserirUsuarioTicket(session);
        System.out.println(" \n --- Fim da  Instalação de usuários --- \n ");
    }

    private void inserirUsuarioLojinha(Session session) throws HibernateException {
        //Usuario Lojinha
        List<Autorizacao>  autorizacoesLojinha = new ArrayList<Autorizacao>();
        autorizacoesLojinha.add(new Autorizacao("ROLE_BOLETO"));        
        autorizacoesLojinha.add(new Autorizacao("ROLE_COMUM"));
        autorizacoesLojinha.add(new Autorizacao("ROLE_LOJINHA"));        

        
        Usuario usuarioLojinha;
        usuarioLojinha = new Usuario("lojinha", "12345", true, autorizacoesLojinha, "1762462", 
                "999.999.999-99","Maria da Lojinha dos Santos","F", "lojinha@ifpi.edu.br", 
                "(89) 9976-1223", "(89) 3521-0253", null, "342", "Rua Tulipas da Costa","São José", 
                "Próximo a Praça Pedro II", "64800-000", new TipoUsuario(new Long(3)), new Cidade(new Long(3425)) );
        
        session.save(usuarioLojinha);
    }

    private void inserirUsuarioProfessor(Session session) throws HibernateException {
        //Usuario Comum
        List<Autorizacao>  autorizacoesComum = new ArrayList<Autorizacao>();        
        autorizacoesComum.add(new Autorizacao("ROLE_BOLETO"));        
        autorizacoesComum.add(new Autorizacao("ROLE_COMUM"));
   
        
        Usuario usuarioProfessor;
        usuarioProfessor = new Usuario("professor", "12345", true, autorizacoesComum, "1762456", 
                "111.111.111-11","Josué da Silva Comum","M", "comum@ifpi.edu.br", 
                "(89) 8873-2615", "(89) 3582-9629", null, "3462", "Rua Granja do Torto","Ipiranga", 
                "Próximo a Ótica Diniz", "64234-000", new TipoUsuario(new Long(2)), new Cidade(new Long(3425)) );
        
        session.save(usuarioProfessor);
    }
    
    
    private void inserirUsuarioAluno(Session session) throws HibernateException {
        //Usuario Comum
        List<Autorizacao>  autorizacoesComum = new ArrayList<Autorizacao>();        
        autorizacoesComum.add(new Autorizacao("ROLE_BOLETO"));        
        autorizacoesComum.add(new Autorizacao("ROLE_COMUM"));
        autorizacoesComum.add(new Autorizacao("ROLE_ALUNO"));
   
        
        Usuario usuarioAluno;
        usuarioAluno = new Usuario("aluna", "12345", true, autorizacoesComum, "S.24.2014", 
                "151.151.151-11","Joseane da Silva Neves","M", "joseane@ifpi.edu.br", 
                "(89) 8873-2615", "(89) 3582-9629", null, "3462", "Rua Granja do Torto","Ipiranga", 
                "Próximo a Ótica Diniz", "64234-000", new TipoUsuario(new Long(1)), new Cidade(new Long(3425)) );
        
        session.save(usuarioAluno);
    }

    private void inserirUsuarioAdmin(Session session) throws HibernateException {
        //Usuario Admin
        List<Autorizacao>  autorizacoesAdmin = new ArrayList<Autorizacao>();
        autorizacoesAdmin.add(new Autorizacao("ROLE_ADM"));
        autorizacoesAdmin.add(new Autorizacao("ROLE_BOLETO"));
        autorizacoesAdmin.add(new Autorizacao("ROLE_LOJINHA"));
        autorizacoesAdmin.add(new Autorizacao("ROLE_COMUM"));
        
        Usuario usuarioAdmin;
        usuarioAdmin = new Usuario("admin", "12345", true, autorizacoesAdmin, "1762468", 
                "012.104.493-90","Wilton Moreira de Santana Júnior","M", "wsantana3@ifpi.edu.br", 
                "(89) 9976-2610", "(89) 3522-0623", null, "33", "Rua José Olegário Correia","Alto da Guia", 
                "Próximo a TV Alvorada", "64800-000", new TipoUsuario(new Long(3)), new Cidade(new Long(3425)) );
        
        session.save(usuarioAdmin);
    }
    
    
    private void inserirUsuarioTicket(Session session) throws HibernateException {
        //Usuario Ticket
        List<Autorizacao>  autorizacoesTicket = new ArrayList<Autorizacao>();
        autorizacoesTicket.add(new Autorizacao("ROLE_TICKET"));        
        autorizacoesTicket.add(new Autorizacao("ROLE_BOLETO"));
        autorizacoesTicket.add(new Autorizacao("ROLE_LOJINHA"));
        autorizacoesTicket.add(new Autorizacao("ROLE_COMUM"));
        
        Usuario usuarioTicket;
        usuarioTicket = new Usuario("ticket", "12345", true, autorizacoesTicket, "1762381", 
                "019.096.603-37","Marina da Silva Vasconcelos","F", "marina@ifpi.edu.br", 
                "(89) 9932-1411", "(89) 3522-2234", null, "123", "Rua Granja do Torto","Morumbi", 
                "Próximo a UFPI", "64800-000", new TipoUsuario(new Long(3)), new Cidade(new Long(3425)) );
        
        session.save(usuarioTicket);
    }
}
