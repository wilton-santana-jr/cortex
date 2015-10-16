package Controller;

import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Mensagem;
import Model.Usuario;
import Thread.ThreadEmail;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class EmailController implements Serializable {

    private String cpf;
    private String captcha;
    private IRepositorioUsuarios usuarioDao;
    private boolean visible;

    public EmailController() {
        cpf = "";
        captcha="";
        usuarioDao = new RepositorioUsuariosBDR();
        //visible=false;
    }

    public void prepararFormDeRecuperacaoDeContaDeUsuario() {
        cpf = "";
        captcha="";
        visible=true;
    }
    
    public void fecharJanelaDeRecuperacaoDeContaDeUsuario() {        
        cpf = "";
        captcha="";
        visible=false;
    }

    public void recuperarCredenciaisDoUsuario() {
        //Aqui onde ocorre o envio de email        
        try {
            
            //Verificar se o captcha informado esta correto
            //Validar cpf informado
            //Verificar se existe usuário para o CPF informado 
            //Verificar ainda se o usuario que foi encontrado possui email cadastrado                                                            
            if(captcha==null || captcha.equalsIgnoreCase("")){
                throw new RuntimeException("Captcha informado é inválido! Preencha o captcha correntamente!");                
            }
            
            if(cpf==null || cpf.equalsIgnoreCase("") || !Utils.Utils.isCPFValido(cpf)){
                throw new RuntimeException("O CPF informado é inválido! Informe um CPF válido!");                
            }
                                   
            Usuario selectedUsuario = usuarioDao.getUsuarioByCPF(cpf);
            
            if(selectedUsuario==null || selectedUsuario.getUsername()==null||selectedUsuario.getUsername().equalsIgnoreCase("")){
                throw new RuntimeException("Não existe usuário cadastrado para o CPF informado! Informe o CPF do usuário corretamente!");                
            }
            
            if(selectedUsuario.getEmail()==null || selectedUsuario.getEmail().equalsIgnoreCase("") || !Utils.Utils.isEmailValido(selectedUsuario.getEmail())){
                throw new RuntimeException("O usuário com o CPF:"+cpf+" não possui email cadastrado no Cortex."
                        + "Por conta disso o memso só poderá recuperar o seu email dirigindo-se ao setor de TI do Campus.");                
            }
            
            
            Mensagem mensagem = new Mensagem();
            mensagem.setDestino(selectedUsuario.getEmail());
            mensagem.setTitulo("Recuperação de Login e Senha de Acesso ao Sistema.");
            String msg = "\nCaro usuário: " + selectedUsuario.getNome()
                    + " suas credenciais de acesso são:\n\n"
                    + "Login: " + selectedUsuario.getUsername() +"\n"
                    + "Senha: " + selectedUsuario.getPassword() +"\n"
                    + "\n\nSeu status de usuário está: " + ((selectedUsuario.isEnable()) ? "Ativo" : "Inativo") + "\n"
                    + "Sendo assim você " + ((selectedUsuario.isEnable()) ? "poderá acessar o sistema sem problemas usando seu login e senha acima informados." : "só poderá acessar o sistema após confirmar seus dados cadastrais na lojinha do campus. Compareça a lojinha do campus para que sua conta seja ativada. Somente após esta confirmação você poderá acessar o sistema.") + ""
                    + "\n\nEquipe Cortex agradece!";
            mensagem.setMensagem(msg);

            if (cpf != null && !cpf.equalsIgnoreCase("")) {
                Thread threadEnviaEmail = new ThreadEmail(mensagem); 
                threadEnviaEmail.start();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Um e-mail contendo os dados de login e senha do usuário será enviado para o email:\" " 
                                                                                    + mensagem.getDestino() + "\". Verifique o seu email para recuperação da senha e do login de acesso ao Cortex!",""));

            cpf = "";
            captcha = "";

        } catch (Exception e) {
            //Caso haja algum erro exibe a mensagem de falha
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);                                    
            captcha = "";
        }

    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    
}
