package Utils;

import Model.Mensagem;
import Thread.ThreadConectaEmail;
import com.sun.mail.smtp.SMTPSSLTransport;
import com.sun.mail.smtp.SMTPTransport;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtils {

    private static String de = "cortex2@ifpi.edu.br"; //coloque aqui um email do gmail valido professor e que permita envio através de outras maquinas
    private static String servidorSmtp = "smtp.gmail.com";
    private static String porta = "465";
    private static String login = de; 
    private static String senha = "08102015"; //coloque aqui a senha do email válida
    private static String mailer = "smtpsend";
    private static Properties props = System.getProperties();
    
    private static SMTPSSLTransport t;
    private static Session session;

    private EmailUtils() throws NoSuchProviderException {
        
        try{
            props.put("mail.smtps.host", servidorSmtp);
            props.put("mail.smtps.auth", "true");
            props.put("mail.smtps.starttls.enable", "false");
            props.put("mail.smtps.port", porta);
            props.put("mail.smtps.socketFactory.port", porta);
            props.put("mail.smtps.socketFactory.fallback", "false");
            props.put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            
            session = Session.getInstance(props, null);
            session.setDebug(false);
            
            //SMTPSSLTransport
            t = (SMTPSSLTransport) session.getTransport("smtps");

            //Tenta conectar no email do Cortex - IFPI
            ThreadConectaEmail threadConectaEmail = new ThreadConectaEmail(t,servidorSmtp, login, senha);
            threadConectaEmail.start();
            
            t = threadConectaEmail.getConnect();
            
            
            
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao carregar as configurações de email do Cortex-IFPI!");
        } 
            
    }

    public static void enviaEmail(Mensagem mensagem) {
        //SMTPTransport t = null;

        try {
            String assunto = mensagem.getTitulo(),
                    conteudo = mensagem.getMensagem(),
                    para = mensagem.getDestino().trim().replaceAll(" ","");

            if(session==null){
                session = Session.getInstance(props, null);
                session.setDebug(false);            
                
            }

            // construct the message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(de, "Cortex - IFPI"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para, false));
            msg.setSubject(assunto);
            msg.setText(conteudo);
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());


            //SMTPTransport
            //t = (SMTPSSLTransport) session.getTransport("smtps");
            if(t==null || !t.isConnected()){
                //SMTPTransport
                t = (SMTPSSLTransport) session.getTransport("smtps");
            }
            
            if(!t.isConnected())
                t.connect(servidorSmtp, login, senha);
            
            t.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {            
            throw new RuntimeException("Ocorreu um erro ao enviar a mensagem para o email: \""
                    + mensagem.getDestino() + "\". Por favor aguarde e tente novamente mais tarde.");
        } 
        


    }
}
