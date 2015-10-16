package Controller;

import Dao.RepositorioAutorizacoesBDR;
import Dao.RepositorioCidadesBDR;
import Dao.RepositorioEstadosBDR;
import Dao.RepositorioTipoUsuariosBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioAutorizacoes;
import Interfaces.dao.IRepositorioCidades;
import Interfaces.dao.IRepositorioEstados;
import Interfaces.dao.IRepositorioTipoUsuarios;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Autorizacao;
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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import selectble.UsuarioDataModel;

@ManagedBean
@ViewScoped
public class UsuarioController implements Serializable {

    private Usuario usuarioEdit;//usado para adicionar ou remover o ou altera usuario
    private Usuario selectedUsuario;
    private List<TipoUsuario> tipoUsuarioList;
    private List<Autorizacao> autorizacaoList;
    private List<Boolean> statusUsuarioList;
    private List<Estado> estadoList;
    private List<Cidade> cidadesOfEstadoList;
    private IRepositorioTipoUsuarios tipoUsuarioDao;
    private IRepositorioUsuarios usuarioDao;
    private IRepositorioAutorizacoes autorizacaoDao;
    private IRepositorioEstados estadoDao;
    private IRepositorioCidades cidadeDao;
    private UsuarioDataModel usuarioDataModel;
    private List<Usuario> filteredUsuarios;
    private IUsuarioValidator usuarioValidator;

    public UsuarioController() {

        usuarioEdit = new Usuario();
        selectedUsuario = new Usuario();

        tipoUsuarioList = new ArrayList<>();
        autorizacaoList = new ArrayList<>();
        statusUsuarioList = new ArrayList<>();
        estadoList = new ArrayList<>();
        cidadesOfEstadoList = new ArrayList<>();

        tipoUsuarioDao = new RepositorioTipoUsuariosBDR();
        autorizacaoDao = new RepositorioAutorizacoesBDR();
        usuarioDao = new RepositorioUsuariosBDR();
        estadoDao = new RepositorioEstadosBDR();
        cidadeDao = new RepositorioCidadesBDR();

        usuarioValidator = new UsuarioValidator();

        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado.getAutorizacoes().contains(new Autorizacao("ROLE_ADM"))) {
            autorizacaoList = autorizacaoDao.list();
        } else {
            autorizacaoList = autorizacaoDao.list("\'ROLE_COMUM\',\'ROLE_BOLETO\'");
        }

        tipoUsuarioList = tipoUsuarioDao.list();

        statusUsuarioList.add(true);
        statusUsuarioList.add(false);


    }

    public List<Usuario> getFilteredUsuarios() {
        return filteredUsuarios;
    }

    public void setFilteredUsuarios(List<Usuario> filteredUsuarios) {
        this.filteredUsuarios = filteredUsuarios;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public Usuario getUsuarioEdit() {
        return usuarioEdit;
    }

    public void setUsuarioEdit(Usuario usuarioEdit) {
        this.usuarioEdit = usuarioEdit;
    }

    public List<TipoUsuario> getTipoUsuarioList() {
        return tipoUsuarioList;
    }

    public void setTipoUsuarioList(List<TipoUsuario> tipoUsuarioList) {
        this.tipoUsuarioList = tipoUsuarioList;
    }

    public List<Boolean> getStatusUsuarioList() {
        return statusUsuarioList;
    }

    public void setStatusUsuarioList(List<Boolean> statusUsuarioList) {
        this.statusUsuarioList = statusUsuarioList;
    }

    //Lista todas as autorizacoes se o usuario for administrador se não so lista as role boleto e comum
    public List<Autorizacao> getAutorizacaoList() {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado.getAutorizacoes().contains(new Autorizacao("ROLE_ADM"))) {
            autorizacaoList = autorizacaoDao.list();

        } else {
            autorizacaoList = autorizacaoDao.list("\'ROLE_COMUM\',\'ROLE_BOLETO\'");
        }


        return autorizacaoList;
    }

    public void setAutorizacaoList(List<Autorizacao> autorizacaoList) {
        this.autorizacaoList = autorizacaoList;
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

    public UsuarioDataModel getUsuarioDataModel() {

        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado.getAutorizacoes().contains(new Autorizacao("ROLE_ADM"))) {
            List<Usuario> lista = usuarioDao.list();
            usuarioDataModel = new UsuarioDataModel(lista, usuarioDao);
        } else {

            List<Autorizacao> listAutorizacao = new ArrayList<>();
            listAutorizacao.add(new Autorizacao("ROLE_COMUM"));
            listAutorizacao.add(new Autorizacao("ROLE_BOLETO"));

            List<Autorizacao> listNotAutorizacao = new ArrayList<>();
            listNotAutorizacao.add(new Autorizacao("ROLE_TICKET"));
            listNotAutorizacao.add(new Autorizacao("ROLE_LOJINHA"));
            listNotAutorizacao.add(new Autorizacao("ROLE_ADM"));

            List<Usuario> lista = usuarioDao.listUsuariosComAutorizacoesNaoPermitidasAndAutorizacoesPermitidas(listNotAutorizacao, listAutorizacao);
            usuarioDataModel = new UsuarioDataModel(lista, usuarioDao);
        }



        return usuarioDataModel;
    }

    public void prepararAdicionarUsuario() {
        usuarioEdit = new Usuario();
        tipoUsuarioList = tipoUsuarioDao.list();
        estadoList = estadoDao.list();
        cidadesOfEstadoList = null;
        cidadesOfEstadoList = new ArrayList<>();
    }

    public void prepararAlterarUsuario(String idUsuario) {
        usuarioEdit = usuarioDao.getUsuario(idUsuario);
        tipoUsuarioList = tipoUsuarioDao.list();
        estadoList = estadoDao.list();
    }

    public void carregarDadosUsuarioLogado() {
        selectedUsuario = getUsuarioLogado();

        try {
            estadoList = estadoDao.list();
            cidadesOfEstadoList = cidadeDao.listCidadesOfEstado(selectedUsuario.getCidadeResidencia().getEstado().getId());
            tipoUsuarioList = tipoUsuarioDao.list();//
        } catch (Exception ex) {
            cidadesOfEstadoList = new ArrayList<>();
        }
    }

    //evento chamado quando seleciona uma linha da tabela de usuarios
    public void onRowSelect(SelectEvent event) {

        Usuario user = (Usuario) event.getObject();

        try {
            estadoList = estadoDao.list();
            cidadesOfEstadoList = cidadeDao.listCidadesOfEstado(user.getCidadeResidencia().getEstado().getId());
        } catch (Exception ex) {
            cidadesOfEstadoList = new ArrayList<>();
        }

        this.selectedUsuario = user;

    }
    
    
    
    public void adicionarUsuarioPelaLojinha() {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {

            usuarioValidator.onCreateUsuarioPelaLojinha(usuarioEdit, usuarioDao, cidadeDao, tipoUsuarioDao, autorizacaoDao);
            usuarioDao.save(usuarioEdit);             
            usuarioDao.criaContaDoUsuario(usuarioEdit);
            
            String msg = "Dados do  usuário(a): " + usuarioEdit.getNome() + " com CPF: " + usuarioEdit.getCpf() + " adicionados com sucesso! ";
            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));
            
            this.selectedUsuario = usuarioEdit;

            this.prepararAdicionarUsuario();


            if (filteredUsuarios != null) {
                filteredUsuarios.add(usuarioDataModel.getRowIndex(), selectedUsuario);
            }

        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }

    }
    

    public void adicionarUsuario() {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {

            usuarioValidator.onCreateUsuario(usuarioEdit, usuarioDao, cidadeDao, tipoUsuarioDao, autorizacaoDao);
            usuarioDao.save(usuarioEdit);                   
            usuarioDao.criaContaDoUsuario(usuarioEdit);
            
            String msg = "Dados do  usuário(a): " + usuarioEdit.getNome() + " com CPF: " + usuarioEdit.getCpf() + " adicionados com sucesso! ";
            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));
            
            this.selectedUsuario = usuarioEdit;

            this.prepararAdicionarUsuario();


            if (filteredUsuarios != null) {
                filteredUsuarios.add(usuarioDataModel.getRowIndex(), selectedUsuario);
            }

        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }

    }

    public void alterarUsuarioLogado() {

        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {

            Usuario usuLogado = getUsuarioLogado();

            //Dados que não podem ser editados pelo usuario Logado
            selectedUsuario.setAutorizacoes(usuLogado.getAutorizacoes());
            selectedUsuario.setTipoUsuario(usuLogado.getTipoUsuario());
            selectedUsuario.setEnable(usuLogado.getEnable());
            selectedUsuario.setUsername(usuLogado.getUsername());
            selectedUsuario.setCpf(usuLogado.getCpf());
            selectedUsuario.setMatricula(usuLogado.getMatricula());
            
            //SE USUSARIO NÃO BOTA NOVA SENHA E PREEENCHE A SENHA ATUAL INFORMAR UM ERRO PREEENCHA A NOVA SENHA
            if (selectedUsuario.getPasswordAtual().equals("")
                    && !selectedUsuario.getPassword().equals("")) {
                String msg = "Senha atual inválida. Informe a senha atual corretamente para cadastrar uma nova senha.";
                throw new RuntimeException(msg);
            }

            //SE USUSARIO BOTA NOVA SENHA E NÃO PREEENCHE A SENHA ATUAL INFORMAR UM ERRO PREEENCHA A SENHA ATUAL
            if (!selectedUsuario.getPasswordAtual().equals("")
                    && selectedUsuario.getPassword().equals("")) {
                String msg = "Nova senha de acesso em branca. Informe a uma nova senha de acesso válida.";
                throw new RuntimeException(msg);
            }


            usuarioValidator.onUpdateUsuario(getUsuarioLogado(), selectedUsuario, usuarioDao, cidadeDao, tipoUsuarioDao, autorizacaoDao);
            usuarioDao.update(selectedUsuario);//usuarioEdit            
            String msg = "Dados do  usuário(a): " + selectedUsuario.getNome() + " com CPF: " + selectedUsuario.getCpf() + " alterados com sucesso! ";



            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));

        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }


    }

    public void alterarUsuario() {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {



            usuarioValidator.onUpdateUsuario(getUsuarioLogado(), selectedUsuario, usuarioDao, cidadeDao, tipoUsuarioDao, autorizacaoDao);
            usuarioDao.update(selectedUsuario);//usuarioEdit            
            //Cria conta do usuário se ela não existir ainda
            usuarioDao.criaContaDoUsuario(selectedUsuario);
            String msg = "Dados do  usuário(a): " + selectedUsuario.getNome() + " com CPF: " + selectedUsuario.getCpf() + " alterados com sucesso! ";



            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));

            if (filteredUsuarios != null) {
                int index = filteredUsuarios.indexOf(selectedUsuario); //captura o indice do usuario com dados antigo na lista de filtrados
                filteredUsuarios.remove(index); //remove o usuario com dados antigo da lista de filtrados
                filteredUsuarios.add(index, selectedUsuario);  //adiciona o novo usuario com seus novos dados na lista de filtrados              
            }


            //Se usuarioLogado estiver editando seus prórios dados
            //deverá atualizar a lista de permissões de acesso que o mesmo tem na sessao do spring security
            Usuario usuarioLogado = getUsuarioLogado();
            if (usuarioLogado.getUsername().equalsIgnoreCase(selectedUsuario.getUsername())) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(usuarioDao.getUsuario(selectedUsuario.getUsername()).getAutorizacoes());
                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }





        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }
    }

    public void alterarUsuarioAtravesDaLojinha() {
        //Obtem o contexto do jsf
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {


            usuarioValidator.onUpdateUsuarioAtravesDaLojinha(getUsuarioLogado(), selectedUsuario, usuarioDao, cidadeDao, tipoUsuarioDao, autorizacaoDao);            
            usuarioDao.update(selectedUsuario);//usuarioEdit    
            usuarioDao.criaContaDoUsuario(selectedUsuario);
            String msg = "Dados do  usuário(a): " + selectedUsuario.getNome() + " com CPF: " + selectedUsuario.getCpf() + " alterados com sucesso! ";
            //exibe mensagem de sucesso
            facesContext.addMessage(null, new FacesMessage(msg));

        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);
        }
    }

    public void handleCityChange(Long idEstado) {
        if (idEstado != null) {
            cidadesOfEstadoList = cidadeDao.listCidadesOfEstado(idEstado);
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

    private Usuario getUsuarioLogado() {

        Usuario usuarioLogado = new Usuario();

        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                if (authentication.getPrincipal() != null) {
                    String userName = ((User) authentication.getPrincipal()).getUsername();
                    usuarioLogado = usuarioDao.getUsuario(userName);
                    usuarioLogado.setUsername(((User) authentication.getPrincipal()).getUsername());
                }
            }
        }
        return usuarioLogado;
    }
}
