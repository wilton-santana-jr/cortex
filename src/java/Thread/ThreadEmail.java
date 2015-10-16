package Thread;

import Model.Mensagem;
import Utils.EmailUtils;


//Thread responsável por enviar email de recuperação de login e senha do usuário
public class ThreadEmail  extends Thread{

    Mensagem mensagem;
    
    public ThreadEmail(Mensagem mensagem) {
        this.mensagem = mensagem;        
    }
    
    @Override
     public void run() {            
         EmailUtils.enviaEmail(mensagem);
     }
     
     
    
}
