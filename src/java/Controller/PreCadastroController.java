package Controller;

import Dao.RepositorioCidadesBDR;
import Dao.RepositorioEstadosBDR;
import Dao.RepositorioTipoUsuariosBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioCidades;
import Interfaces.dao.IRepositorioEstados;
import Interfaces.dao.IRepositorioTipoUsuarios;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Cidade;
import Model.Estado;
import Model.TipoUsuario;
import Model.Usuario;
import Utils.Utils;
import Validator.UsuarioValidator;
import Interfaces.validator.IUsuarioValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@ViewScoped
public class PreCadastroController implements Serializable {

    private Usuario usuario;
    
    private List<TipoUsuario> tipoUsuarioList;
    private List<Estado> estadoList;
    private List<Cidade> cidadesOfEstadoList;
    
    private IRepositorioTipoUsuarios tipoUsuarioDao;
    private IRepositorioEstados estadoDao;
    private IRepositorioCidades cidadeDao;
    private IRepositorioUsuarios usuarioDao;
    
    private IUsuarioValidator usuarioValidator;

    @PostConstruct
    public void initialize() {
        if (tipoUsuarioList == null && tipoUsuarioDao == null) {
            tipoUsuarioList = new ArrayList<>();
            tipoUsuarioDao = new RepositorioTipoUsuariosBDR();
        }
        this.tipoUsuarioList = tipoUsuarioDao.list();
    }

    public PreCadastroController() {
        usuario = new Usuario();
        
        tipoUsuarioList = new ArrayList<>();
        estadoList = new ArrayList<>();
        cidadesOfEstadoList = new ArrayList<>();
        
        tipoUsuarioDao = new RepositorioTipoUsuariosBDR();
        estadoDao = new RepositorioEstadosBDR();
        cidadeDao = new RepositorioCidadesBDR();
        usuarioDao = new RepositorioUsuariosBDR();
        
        usuarioValidator = new UsuarioValidator();
        
        tipoUsuarioList = tipoUsuarioDao.list();
        estadoList = estadoDao.list();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<TipoUsuario> getTipoUsuarioList() {
        return tipoUsuarioList;
    }

    public void setTipoUsuarioList(List<TipoUsuario> tipoUsuarioList) {
        this.tipoUsuarioList = tipoUsuarioList;
    }

    public List<Estado> getEstadoList() {
        return estadoList;
    }

    public void setEstadoList(List<Estado> estadoList) {
        this.estadoList = estadoList;
    }

    public List<Cidade> getCidadesOfEstadoList() {
        return cidadesOfEstadoList;
    }

    public void setCidadesOfEstadoList(List<Cidade> cidadesOfEstadoList) {
        this.cidadesOfEstadoList = cidadesOfEstadoList;
    }
    

    public void prepararPreCadastroUsuario(ActionEvent actionEvent) {
        usuario = new Usuario();
        tipoUsuarioList = tipoUsuarioDao.list();
        estadoList = estadoDao.list();        
    }

    /**
     * Metodo chamado pelo botão salvar do formulario de pré-cadastro de usuario
     * no cortex;
     */
    public void salvar(ActionEvent actionEvent) {
        
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {

           
            usuarioValidator.onCreatePreCadatro(usuario, usuarioDao, cidadeDao, tipoUsuarioDao);     
            usuarioDao.savePreCadastro(usuario);                       
            usuarioDao.criaContaDoUsuario(usuario);
            
                         

            String obs = " Observações: A sua conta encontra-se bloqueada, para desbloquear a sua conta e utilizar o sistema digira-se "
                    + "a lojinha do campus com seus documentos pessoais (cpf, identidade, matrícula do curso) para confirmar o seu cadastramento.";

            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage("Pré-cadastro do(a) usuário(a): \"" + usuario.getNome() + "\" com CPF: \""
                    + usuario.getCpf()
                    + "\" adicionado(a) com sucesso! " + obs));

            //instancia um novo objeto inscricao para iniciar um novo cadastro
            usuario = new Usuario();
            tipoUsuarioList = tipoUsuarioDao.list();            

        } catch (Exception e) {
            //Caso haja algum erro exibe a mensagem de falha
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }



    }

    public void handleCityChange() {
        if (usuario.getCidadeResidencia() != null
                && usuario.getCidadeResidencia().getEstado() != null
                && usuario.getCidadeResidencia().getEstado().getId() != null) {
            cidadesOfEstadoList = cidadeDao.listCidadesOfEstado(usuario.getCidadeResidencia().getEstado().getId());
        } else {
            cidadesOfEstadoList = new ArrayList<>();
        }
    }
    
    
    /**
     * Método usado para validar campos com espaços em branco.
     *
     * @param contexto
     * @param componente
     * @param valor
     */
    public void validarCampoEspacoBranco(FacesContext contexto, UIComponent componente, Object valor) {
        String valorString = (String) valor;
        if (valorString.trim().equals("") || valorString.trim().contains(" ")) {
            ((UIInput) componente).setValid(false);
            String mensagem = componente.getAttributes().get("label") + ": Valor inválido, preencha com caracteres diferentes de espaço.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, mensagem);
            contexto.addMessage(componente.getClientId(contexto), facesMessage);
        }
    }

    /**
     * Método usado para validar campos em branco.
     *
     * @param contexto
     * @param componente
     * @param valor
     */
    public void validarEspacoBranco(FacesContext contexto, UIComponent componente, Object valor) {
        String valorString = (String) valor;
        if (valorString.trim().equals("")) {
            ((UIInput) componente).setValid(false);
            String mensagem = componente.getAttributes().get("label") + ": Valor inválido, preencha com caracteres diferentes de espaço.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, mensagem);
            contexto.addMessage(componente.getClientId(contexto), facesMessage);
        }
    }

    /**
     * Método usado para validar email
     *
     * @param contexto
     * @param componente
     * @param valor
     */
    public void validarEmail(FacesContext contexto, UIComponent componente, Object valor) {
        String valorString = (String) valor;
        if (valorString != null && !(valorString.equalsIgnoreCase("")) && !Utils.isEmailValido(valorString)) {
            ((UIInput) componente).setValid(false);
            String mensagem = componente.getAttributes().get("label") + ": Valor inválido, preencha com um email válido.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, mensagem);
            contexto.addMessage(componente.getClientId(contexto), facesMessage);
        }
    }

}
