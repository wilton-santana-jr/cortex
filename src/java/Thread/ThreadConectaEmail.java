package Thread;

import Model.Mensagem;
import Utils.EmailUtils;
import com.sun.mail.smtp.SMTPSSLTransport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

public class ThreadConectaEmail extends Thread{

    String  servidorSmtp,login,senha;
    SMTPSSLTransport t;
            
    public ThreadConectaEmail(SMTPSSLTransport t,String servidorSmtp,String login,String senha) {
        this.t=t;
        this.servidorSmtp=servidorSmtp;
        this.login=login;
        this.senha=senha;
    }
    
    @Override
     public void run() {         
          if(!t.isConnected()){
              try {
                  t.connect(servidorSmtp, login, senha);
              } catch (MessagingException ex) {
                     throw new RuntimeException("Ocorreu um erro ao conectar ao email do Cortex-IFPI!");
              }
          }      
     }
    
    public SMTPSSLTransport getConnect(){
        return t;
    }
     
     
    
}
