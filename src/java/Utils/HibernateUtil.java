package Utils;


import Install.AutorizacaoInstall;
import Install.CidadeInstall;
import Install.ContaInstall;
import Install.EstadoInstall;
import Install.PaisInstall;
import Install.RegistroOperacaoInstall;
import Install.TicketInstall;
import Install.TipoOperacaoInstall;
import Install.TipoRefeicaoInstall;
import Install.TipoUsuarioInstall;
import Install.UsuarioInstall;
import Install.VendaTicketInstall;
import Model.Autorizacao;
import Model.Cidade;
import Model.Conta;
import Model.Estado;
import Model.Pais;
import Model.RegistroOperacao;
import Model.Ticket;
import Model.TipoOperacao;
import Model.TipoRefeicao;
import Model.TipoUsuario;
import Model.Usuario;
import Model.VendaTicket;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;


public class HibernateUtil {

    private static SessionFactory sessionFactory;

    

    private HibernateUtil() {
        
    }

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                // Create the SessionFactory from standard (hibernate.cfg.xml)
                // config file.
                AnnotationConfiguration ac = new AnnotationConfiguration();                
                
                ac.addAnnotatedClass(Pais.class);
                ac.addAnnotatedClass(Estado.class);
                ac.addAnnotatedClass(Cidade.class);
                
                
                ac.addAnnotatedClass(Autorizacao.class);
                ac.addAnnotatedClass(TipoUsuario.class);
                ac.addAnnotatedClass(Usuario.class);               
                
                ac.addAnnotatedClass(Conta.class); 
                ac.addAnnotatedClass(TipoOperacao.class); 
                ac.addAnnotatedClass(RegistroOperacao.class); 
                
                
                ac.addAnnotatedClass(TipoRefeicao.class); 
                ac.addAnnotatedClass(Ticket.class);
                ac.addAnnotatedClass(VendaTicket.class);
                
                
                sessionFactory = ac.configure().buildSessionFactory();
                
                //se quiser regerar o banco e inserir os dados coloque true true
                //caso contrário coloque false, false
                //Em produção deverá ficar false false
//                SchemaExport se = new SchemaExport(ac);
//                se.create(true, true);
                

            } catch (Throwable ex) {
                // Log the exception.
                sessionFactory.close();
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }

            return sessionFactory;

        } else {
            return sessionFactory;
        }
        
    }
    
    public static Session getSession() throws HibernateException {
        Session openSession = HibernateUtil.getSessionFactory().openSession();
        return openSession;
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();                
        
//        PaisInstall paisInstall = new PaisInstall(getSession());
//        EstadoInstall estadoInstall = new EstadoInstall(getSession());
//        CidadeInstall cidadeInstall = new CidadeInstall(getSession());        
//        TipoUsuarioInstall tipoUsuarioInstall = new TipoUsuarioInstall(getSession());
//        
//        AutorizacaoInstall autorizacaoInstall = new AutorizacaoInstall(getSession());                
//        UsuarioInstall usuarioInstall = new UsuarioInstall(getSession());        
//        
//        
//        ContaInstall contaInstall = new ContaInstall(getSession());  
//        TipoOperacaoInstall tipoOperacaoInstall = new TipoOperacaoInstall(getSession());
//        RegistroOperacaoInstall registroOperacaoInstall = new RegistroOperacaoInstall((getSession()));
//        
//        TipoRefeicaoInstall tipoRefeicaoInstall = new TipoRefeicaoInstall(getSession());
//        TicketInstall ticketInstall = new TicketInstall((getSession()));
//        VendaTicketInstall vendaTicketInstall = new VendaTicketInstall(getSession());
        
                
        
        
    }

}
