package Model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "SEQ_GEN_TIPOOPERACAO", sequenceName = "SEQ_GEN_TIPOOPERACAO", allocationSize = 1)
public class TipoOperacao implements Serializable {
     @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_TIPOOPERACAO")
    private  Long id;    
    
    @Column(unique = true, nullable = false)
    private String nome; //CREDITO / DEBITO
    
    @Column(unique = true, nullable = false)
    private String abreviacao; //C-CREDITO / D-DEBITO

    public TipoOperacao() {
    }

    public TipoOperacao(Long id) {
        this.id = id;
    }
    
    
    
    public TipoOperacao(Long id, String nome, String abreviacao) {
        this.id = id;
        this.nome = nome;
        this.abreviacao = abreviacao;
    }

    public TipoOperacao(String nome, String abreviacao) {
        this.nome = nome;
        this.abreviacao = abreviacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TipoOperacao other = (TipoOperacao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
