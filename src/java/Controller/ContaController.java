package Controller;

import Dao.RepositorioTipoOperacoesBDR;
import Dao.RepositorioContasBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioContas;
import Interfaces.dao.IRepositorioTipoOperacoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Conta;
import Model.RegistroOperacao;
import Model.TipoOperacao;
import Model.Usuario;
import Utils.HibernateUtil;
import Validator.ContaValidator;
//import Validator.ContaValidator;
import Interfaces.validator.IContaValidator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ManagedBean
@RequestScoped
public class ContaController implements Serializable {

    private BigDecimal saldo;
    private BigDecimal saldoAnteriorExtrato;
    private BigDecimal saldoTotalExtrato;
    private Date dataFormIni;
    private Date dataFormFim;
    private Date dataFormIniMin;
    private Date dataFormFimMax;
    private String valorBoleto;
    private Usuario selectedUsuario;
    private RegistroOperacao registroOperacao;
    private List<RegistroOperacao> listRegistroOperacao;
    private IRepositorioContas contaDao = new RepositorioContasBDR();
    private IRepositorioUsuarios usuarioDao = new RepositorioUsuariosBDR();
    private IRepositorioTipoOperacoes tipoOperacaoDao = new RepositorioTipoOperacoesBDR();
    private IContaValidator contaValidator;

    public ContaController() {

        Calendar dataInicioMin = Calendar.getInstance();
        dataInicioMin.add(Calendar.MONTH, -3);
        Calendar dataFimMin = Calendar.getInstance();

        dataFormIniMin = dataInicioMin.getTime();
        dataFormFimMax = dataFimMin.getTime();

        dataFormIni = Calendar.getInstance().getTime();
        dataFormFim = Calendar.getInstance().getTime();

        saldo = new BigDecimal(0.0);
        saldoAnteriorExtrato = new BigDecimal(0.0);
        saldoTotalExtrato = new BigDecimal(0.0);
        valorBoleto = "R$0,00";

        selectedUsuario = new Usuario();
        registroOperacao = new RegistroOperacao();
        listRegistroOperacao = new ArrayList<>();

        contaDao = new RepositorioContasBDR();
        usuarioDao = new RepositorioUsuariosBDR();
        tipoOperacaoDao = new RepositorioTipoOperacoesBDR();

        contaValidator = new ContaValidator();

    }

    
    public void prepararFormCreditarBoletoByUsuario(String loginUserSelecionado) {

        if (loginUserSelecionado != null) {
            selectedUsuario = usuarioDao.getUsuario(loginUserSelecionado);
        }

    }

    
    public void creditarBoletoByUsuario() {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        Usuario usuarioOperador = getUsuarioLogado();//usuário responsável pela inserção dos créditos na conta do usuário selecionado
        selectedUsuario = usuarioDao.getUsuario(selectedUsuario.getUsername());
        TipoOperacao tipoOperacaoCredito = tipoOperacaoDao.getTipoOperacao("C");//RETORNA O TIPO DE OPERAÇÃO DE CREDITO        

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();

        try {

            contaValidator.onCreditarBoletoByUsuario(usuarioOperador, selectedUsuario, valorBoleto, registroOperacao, usuarioDao, contaDao, tipoOperacaoDao);

            Conta contaUsuarioSelecionado = contaDao.getContaByUsuario(selectedUsuario, session);

            registroOperacao.setUsuarioOperador(usuarioOperador);
            registroOperacao.setConta(contaUsuarioSelecionado);
            registroOperacao.setTipoOperacao(tipoOperacaoCredito);
            registroOperacao.setDataRegistro(Calendar.getInstance());
            BigDecimal saldoAtual = contaUsuarioSelecionado.getSaldo();

            BigDecimal valorBoletoPagamento = stringToBigDecimal(valorBoleto);

            registroOperacao.setValor(valorBoletoPagamento);
            registroOperacao.setSaldoAnterior(saldoAtual);

            BigDecimal novoSaldo = saldoAtual.add(valorBoletoPagamento);
            contaUsuarioSelecionado.setSaldo(novoSaldo);
            contaUsuarioSelecionado.setDataAtualizacao(Calendar.getInstance());

            session.save(registroOperacao);
            session.update(contaUsuarioSelecionado);

            t.commit();

            //limpa os campos e exibe a mensagem de sucesso que os créditos foram inseridos com sucesso            

            String msg = "O Operador: "+registroOperacao.getUsuarioOperador().getNome()+" registrou com sucesso o Boleto: "+registroOperacao.getBoleto()+" no valor de R$ " + valorBoleto + " na conta do usuário(a): \"" + contaUsuarioSelecionado.getUsuario().getNome() + "\" de CPF: " + contaUsuarioSelecionado.getUsuario().getCpf() + "! ";
            facesContext.addMessage(null, new FacesMessage(msg));

            valorBoleto = "R$0,00";
            registroOperacao = new RegistroOperacao();

        } catch (Exception e) {
            t.rollback();//desfaz todas as operações realizadas durante a transalção dos créditos do usuário                        
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
            facesContext.addMessage(null, facesMessage);            
        } finally {
            session.close();
        }

    }

    public void prepararGerarExtratoUsuarioLogado() {
        saldoAnteriorExtrato = new BigDecimal(0.0);
        saldoTotalExtrato = new BigDecimal(0.0);
        listRegistroOperacao = new ArrayList<>();
    }

    public List<RegistroOperacao> exibirExtratoUsuarioLogado() {
        Usuario usuarioLogado = getUsuarioLogado();

        if (usuarioLogado != null) {

            Calendar dataInicio = Calendar.getInstance();
            Calendar dataFim = Calendar.getInstance();

            dataInicio.setTime(dataFormIni);
            dataInicio.set(Calendar.HOUR_OF_DAY, 0);
            dataInicio.set(Calendar.MINUTE, 0);
            dataInicio.set(Calendar.SECOND, 0);

            dataFim.setTime(dataFormFim);
            dataFim.set(Calendar.HOUR_OF_DAY, 24);
            dataFim.set(Calendar.MINUTE, 59);
            dataFim.set(Calendar.SECOND, 59);

            listRegistroOperacao = contaDao.getExtrato(usuarioLogado, dataInicio, dataFim);

            if (listRegistroOperacao != null && !listRegistroOperacao.isEmpty()) {

                saldoAnteriorExtrato = listRegistroOperacao.get(0).getSaldoAnterior();

                if (listRegistroOperacao.get(listRegistroOperacao.size() - 1).getTipoOperacao().getAbreviacao().equalsIgnoreCase("C")) {
                    saldoTotalExtrato = listRegistroOperacao.get(listRegistroOperacao.size() - 1).getSaldoAnterior().add(listRegistroOperacao.get(listRegistroOperacao.size() - 1).getValor());
                } else {
                    saldoTotalExtrato = listRegistroOperacao.get(listRegistroOperacao.size() - 1).getSaldoAnterior().subtract(listRegistroOperacao.get(listRegistroOperacao.size() - 1).getValor());
                }

            }

        } else {
            listRegistroOperacao = null;
        }
        return listRegistroOperacao;
    }

    public BigDecimal exibirSaldoUsuarioLogado() {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado != null) {
            saldo = contaDao.getSaldo(usuarioLogado);
        } else {
            saldo = new BigDecimal(0.0);
        }
        return saldo;
    }

    public void exibirSaldoUsuarioSelecionado(String login) {
        if (login != null) {

            selectedUsuario = usuarioDao.getUsuario(login);
            saldo = contaDao.getSaldo(selectedUsuario);
            saldo = (saldo == null)?new BigDecimal(0.0):saldo;

        } else {
            saldo = new BigDecimal(0.0);
        }
    }

    public String getValorBoleto() {
        return valorBoleto;
    }

    public void setValorBoleto(String valorBoleto) {
        this.valorBoleto = valorBoleto;
    }

    public RegistroOperacao getRegistroOperacao() {
        return registroOperacao;
    }

    public void setRegistroOperacao(RegistroOperacao registroOperacao) {
        this.registroOperacao = registroOperacao;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public Date getDataFormIniMin() {
        return dataFormIniMin;
    }

    public void setDataFormIniMin(Date dataFormIniMin) {
        this.dataFormIniMin = dataFormIniMin;
    }

    public Date getDataFormFimMax() {
        return dataFormFimMax;
    }

    public void setDataFormFimMax(Date dataFormFimMax) {
        this.dataFormFimMax = dataFormFimMax;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public BigDecimal getSaldoTotalExtrato() {
        return saldoTotalExtrato;
    }

    public BigDecimal getSaldoAnteriorExtrato() {
        return saldoAnteriorExtrato;
    }

    public Date getDataFormIni() {
        return dataFormIni;
    }

    public Date getDataFormFim() {
        return dataFormFim;
    }

    public void setDataFormIni(Date dataFormIni) {
        this.dataFormIni = dataFormIni;
    }

    public void setDataFormFim(Date dataFormFim) {
        this.dataFormFim = dataFormFim;
    }

    public List<RegistroOperacao> getListRegistroOperacao() {
        return listRegistroOperacao;
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

    private BigDecimal stringToBigDecimal(final String formattedString) {
        final DecimalFormatSymbols symbols;
        final char groupSeparatorChar;
        final String groupSeparator;
        final char decimalSeparatorChar;
        final String decimalSeparator;
        String fixedString;
        final BigDecimal number;
        final Locale locale = new Locale("pt", "BR"); //Define o locale como brasil

        symbols = new DecimalFormatSymbols(locale);
        groupSeparatorChar = symbols.getGroupingSeparator();
        decimalSeparatorChar = symbols.getDecimalSeparator();

        if (groupSeparatorChar == '.') {
            groupSeparator = "\\" + groupSeparatorChar;
        } else {
            groupSeparator = Character.toString(groupSeparatorChar);
        }

        if (decimalSeparatorChar == '.') {
            decimalSeparator = "\\" + decimalSeparatorChar;
        } else {
            decimalSeparator = Character.toString(decimalSeparatorChar);
        }

        fixedString = formattedString.replaceAll(groupSeparator, "");
        fixedString = fixedString.replaceAll(decimalSeparator, ".");
        number = new BigDecimal(fixedString);

        return (number);
    }

    /**
     * Método usado para validar e inserir os digitos verificadores dos boletos
     * que são pagos pelo usuário.
     *
     * @param contexto
     * @param componente
     * @param valor
     */
    public void validarBoletoGru(FacesContext contexto, UIComponent componente, Object valor) {
        String numeroBoletoGru = (String) valor;


        if (numeroBoletoGru.trim().equals("")) {
            ((UIInput) componente).setValid(false);
            String mensagem = componente.getAttributes().get("label") + ": Valor inválido, preencha com caracteres diferentes de espaço.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, mensagem);
            contexto.addMessage(componente.getClientId(contexto), facesMessage);
        }
    }

    
}
