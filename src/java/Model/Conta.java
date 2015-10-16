package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
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
@SequenceGenerator(name = "SEQ_GEN_CONTA", sequenceName = "SEQ_GEN_CONTA", allocationSize = 1)
public class Conta implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_CONTA")
    private  Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuarioID", nullable = false, unique = true)
    private Usuario usuario;
    
    private BigDecimal saldo;
    
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataAtualizacao; //data de criacao da conta

    
    
    public Conta() {
    }

    public Conta(Long id) {
        this.id = id;
    }

    public Conta(Long id, BigDecimal novoSaldo,Calendar dataAtualizacao) {
        this.id = id;
        this.saldo = novoSaldo;
        this.dataAtualizacao = dataAtualizacao;
    }
    
    
    
    
    
    
    public Conta(Long id, String loginUsuario, BigDecimal saldo,Calendar dataAtualizacao) {
        this.id=id;
        this.usuario = new Usuario(loginUsuario);
        this.saldo = saldo;
        this.dataAtualizacao = dataAtualizacao;
    }
       
    public Conta(String loginUsuario, BigDecimal saldo,Calendar dataAtualizacao) {
        this.usuario = new Usuario(loginUsuario);
        this.saldo = saldo;
        this.dataAtualizacao = dataAtualizacao;
    }
    
    public Conta(Usuario usuario, BigDecimal saldo,Calendar dataCriacao) {
        this.usuario = usuario;
        this.saldo = saldo;
        this.dataAtualizacao = dataCriacao;
    }

    public Conta(Usuario usuario) {
        this.usuario = usuario;        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Calendar getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Calendar dataAtualizacao) {
        dataAtualizacao.set(Calendar.SECOND, 0);
        dataAtualizacao.set(Calendar.MILLISECOND, 0);
        this.dataAtualizacao = dataAtualizacao;
    }



    
    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Conta other = (Conta) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
