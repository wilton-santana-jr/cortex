package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;


@Entity
public class Usuario implements Serializable {

    @Id
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "enable", columnDefinition = "BOOLEAN")
    private boolean enable;
    
    //cascade = { CascadeType.PERSIST, CascadeType.MERGE }
    @ManyToMany(fetch = FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(            
     name = "usuario_autorizacao",
     joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "autorizacao_id")
    )
    private List<Autorizacao> autorizacoes;
    
    @Column(length = 14, nullable = false, unique = true)
    private String matricula; 
    @Column(length = 14, nullable = false, unique = true)
    private String cpf;
    private String nome;
    private String sexo;
    private String email;
    
    private String fone1;
    private String fone2;
    private String fone3;
    private String numeroDaResidencia;
    private String logradouro;
    private String bairro;
    private String Complemento;    
    private String cep;
    
    @Column(nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataBloqueio;
    
    @Column(nullable = true)    
    private String motivoBloqueio;
    
    @ManyToOne
    @JoinColumn(name = "tipoUsuarioID", nullable = true, unique = false)
    private TipoUsuario tipoUsuario;
    
    @ManyToOne
    @JoinColumn(name = "cidadeResidenciaID", nullable = true, unique = false)
    private Cidade cidadeResidencia;
    
    
    @Transient
    private String passwordAtual;
    
    

    public Usuario() {
        tipoUsuario = (tipoUsuario==null)?new TipoUsuario():tipoUsuario;
        cidadeResidencia = (cidadeResidencia==null)?new Cidade():cidadeResidencia;
        enable = false;
        autorizacoes = (autorizacoes==null)?new ArrayList<Autorizacao>():autorizacoes;
    }

    public String getPasswordAtual() {
        return passwordAtual;
    }

    public void setPasswordAtual(String passwordAtual) {
        this.passwordAtual = passwordAtual;
    }
    
    

    public Usuario(String username) {
        this.username = username.trim().replaceAll(" ","");
    }
    
    

    public Usuario(String username, String password, boolean enable, List<Autorizacao> autorizacoes, String matricula, String cpf, String nome, String sexo, String email, String fone1, String fone2, String fone3, String numeroDaResidencia, String logradouro, String bairro, String Complemento, String cep, TipoUsuario tipoUsuario, Cidade cidadeResidencia) {
        this.username = username.trim().replaceAll(" ", "");
        this.password = password;
        this.enable = enable;
        this.autorizacoes = autorizacoes;
        this.matricula = matricula;
        this.cpf = cpf;
        this.nome = nome;
        this.sexo = sexo;
        this.email = (email!=null)?email.toLowerCase():null;
        this.fone1 = fone1;
        this.fone2 = fone2;
        this.fone3 = fone3;
        this.numeroDaResidencia = numeroDaResidencia;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.Complemento = Complemento;
        this.cep = cep;
        this.tipoUsuario = tipoUsuario;
        this.cidadeResidencia = cidadeResidencia;
    }
    
    

    public Usuario(String username, String password, boolean enable, List<Autorizacao> autorizacoes) {
        this.username = username.trim().replaceAll(" ", "");
        this.password = password;
        this.enable = enable;
        this.autorizacoes = autorizacoes;
    }

    public Usuario(String username, String password, boolean enable, List<Autorizacao> autorizacoes, TipoUsuario tipoUsuario, Cidade cidadeResidencia) {
        this.username = username.trim().replaceAll(" ", "");
        this.password = password;
        this.enable = enable;
        this.autorizacoes = autorizacoes;
        this.tipoUsuario = tipoUsuario;
        this.cidadeResidencia = cidadeResidencia;
    }
    
    

    public List<Autorizacao> getAutorizacoes() {
        autorizacoes = (autorizacoes!=null)?(autorizacoes):(new ArrayList<Autorizacao>());                   
        return autorizacoes;
    }

    public void setAutorizacoes(List<Autorizacao> autorizacoes) {
        this.autorizacoes = autorizacoes;
    }

    public boolean isEnable() {
        return enable;
    }
    
    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
   

    public String getUsername() {
        if(username!=null)
            return username.trim();
        return username;
    }

    public void setUsername(String username) {
        if(username!=null)
            this.username = username.trim();        
    }
    
    
    
    ////////////////////
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return (email!=null)?email.toLowerCase():null;
    }

    public void setEmail(String email) {
        this.email = (email!=null)?email.toLowerCase():null;
    }

    public String getFone1() {
        return fone1;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public String getFone3() {
        return fone3;
    }

    public void setFone3(String fone3) {
        this.fone3 = fone3;
    }

    public String getNumeroDaResidencia() {
        return numeroDaResidencia;
    }

    public void setNumeroDaResidencia(String numeroDaResidencia) {
        this.numeroDaResidencia = numeroDaResidencia;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String Complemento) {
        this.Complemento = Complemento;
    }    

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Cidade getCidadeResidencia() {
        return cidadeResidencia;
    }

    public void setCidadeResidencia(Cidade cidadeResidencia) {
        this.cidadeResidencia = cidadeResidencia;
    }
    
     public Calendar getDataBloqueio() {
        return dataBloqueio;
    }

    public void setDataBloqueio(Calendar dataBloqueio) {
        dataBloqueio.set(Calendar.SECOND, 0);
        dataBloqueio.set(Calendar.MILLISECOND, 0);
        this.dataBloqueio = dataBloqueio;
    }

    public String getMotivoBloqueio() {
        return motivoBloqueio;
    }

    public void setMotivoBloqueio(String motivoBloqueio) {
        this.motivoBloqueio = motivoBloqueio;
    }
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.username != null ? this.username.hashCode() : 0);
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
        final Usuario other = (Usuario) obj;
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "username=" + username + '}';
    }
    
    
    
    

}
