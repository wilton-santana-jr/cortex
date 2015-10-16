package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

@Entity
@SequenceGenerator(name = "SEQ_GEN_REGOPE", sequenceName = "SEQ_GEN_REGOPE", allocationSize = 1)
public class RegistroOperacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_REGOPE")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "contaId", nullable = false, unique = false)
    private Conta conta;
    @ManyToOne
    @JoinColumn(name = "tipoOperacaoId", nullable = false, unique = false)
    private TipoOperacao tipoOperacao;    
    @Column(nullable = false, unique = false)
    private BigDecimal valor;    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataRegistro;
    //55
    @Column(length = 55, nullable = true, unique = true)
    private String boleto;//numero do boleto caso operacao seja crédito ou nulo caso a operação seja débito
    @Column(nullable = false, unique = false)
    private BigDecimal saldoAnterior; //saldo anterior a realizacao da operação;
    @ManyToOne
    @JoinColumn(name = "usuarioOperadorId", nullable = false, unique = false)
    private Usuario usuarioOperador; //usuario responsável por creditar ou debitar o saldo do cliente da conta especificada. O chamado vulgo Usuário Lojinha

    public RegistroOperacao() {
    }

    public RegistroOperacao(Long idConta, Long idTipoOperacao, BigDecimal valor, Calendar dataRegistro, String boleto, BigDecimal saldoAnterior) {
        this.conta = new Conta(idConta);
        this.tipoOperacao = new TipoOperacao(idTipoOperacao);
        this.valor = valor;
        dataRegistro.set(Calendar.SECOND, 0);
        dataRegistro.set(Calendar.MILLISECOND, 0);
        this.dataRegistro = dataRegistro;
        this.boleto = boleto;
        this.saldoAnterior = saldoAnterior;
    }

    public RegistroOperacao(Long idConta, Long idTipoOperacao, BigDecimal valor, Calendar dataRegistro, String boleto, BigDecimal saldoAnterior, String loginUsuarioOperador) {
        this.conta = new Conta(idConta);
        this.tipoOperacao = new TipoOperacao(idTipoOperacao);
        this.valor = valor;
        dataRegistro.set(Calendar.SECOND, 0);
        dataRegistro.set(Calendar.MILLISECOND, 0);
        this.dataRegistro = dataRegistro;
        this.boleto = boleto;
        this.saldoAnterior = saldoAnterior;
        this.usuarioOperador = new Usuario(loginUsuarioOperador);
    }

    public RegistroOperacao(Conta conta, TipoOperacao tipoOperacao, BigDecimal valor, Calendar dataRegistro, String boleto, BigDecimal saldoAnterior) {
        this.conta = conta;
        this.tipoOperacao = tipoOperacao;
        this.valor = valor;
        dataRegistro.set(Calendar.SECOND, 0);
        dataRegistro.set(Calendar.MILLISECOND, 0);
        this.dataRegistro = dataRegistro;
        this.boleto = boleto;
        this.saldoAnterior = saldoAnterior;
    }

    public RegistroOperacao(Conta conta, TipoOperacao tipoOperacao, BigDecimal valor, Calendar dataRegistro, String boleto, BigDecimal saldoAnterior, Usuario usuarioOperador) {
        this.conta = conta;
        this.tipoOperacao = tipoOperacao;
        this.valor = valor;
        dataRegistro.set(Calendar.SECOND, 0);
        dataRegistro.set(Calendar.MILLISECOND, 0);
        this.dataRegistro = dataRegistro;
        this.boleto = boleto;
        this.saldoAnterior = saldoAnterior;
        this.usuarioOperador = usuarioOperador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
   
    

    public Calendar getDataRegistro() {
        return dataRegistro;
    }
    
    public Date getDataRegistroDate() {
        return dataRegistro.getTime();
    }

    public String getDataRegistroToString() {
        String dataFormatada = "";
        if (dataRegistro != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date data = dataRegistro.getTime();
            dataFormatada = sdf.format(data);
        }
        return dataFormatada;
    }


    public void setDataRegistro(Calendar dataRegistro) {
        dataRegistro.set(Calendar.SECOND, 0);
        dataRegistro.set(Calendar.MILLISECOND, 0);
        this.dataRegistro = dataRegistro;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Usuario getUsuarioOperador() {
        return usuarioOperador;
    }

    public void setUsuarioOperador(Usuario usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegistroOperacao other = (RegistroOperacao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
