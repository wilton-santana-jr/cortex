package Model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class Autorizacao implements Serializable, GrantedAuthority {

    @Id
    private String nome;
    private String descricao;

    public Autorizacao() {
    }
    
    
    
    public Autorizacao(String nome) {
        this.nome = nome.trim();        
    }

    public Autorizacao(String nome, String descricao) {
        this.nome = nome.trim();
        this.descricao = descricao.trim();
    }

    public String getNome() {
        return nome.trim();
    }

    public void setNome(String nome) {
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao.trim();
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.nome != null ? this.nome.hashCode() : 0);
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
        final Autorizacao other = (Autorizacao) obj;
        if ((this.nome == null) ? (other.nome != null) : !this.nome.trim().equals(other.nome.trim())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Autorizacao{" + "nome=" + nome.trim() + '}';
    }

    @Override
    public String getAuthority() {
        return this.getNome().trim();        
    }
    
    
    
}
